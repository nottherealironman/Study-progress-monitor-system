import java.net.*;
import java.io.*;
import java.util.*;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;

public class Client {
  // Declaration of member variables
  private static SubjectList subjList;
  private static StudentList studList;
  private static GradeList grdList;
  private static GradedAssessment grdAssmnt;
  private static LinkedList<Subject> subjects;
  private static LinkedList<Student> students;
  private static LinkedList<Grade> grades;
  private static LinkedList<String> menuList;
  private static PublicKey publicKey;

  // Method to display assessment for a subject
  private static void displayAssessment(String subject){
    System.out.printf("Assignment of %s:\n\n",subject);
    
    // Loop through the linked list of subjects
    for (Subject subj : subjects) {
      // If subject name in list matches the subject passed in argument then display assessment
      if(subj.getName().equals(subject)){
        for (Assessment assessment : subj.getAssessment()) {
          System.out.println(assessment);
        }
      }
    }
  }

  // Method to display subject list enrolled by student
  private static void displaySubject(Student student, String option, ObjectOutputStream outObj, ObjectInputStream inObj){
    Scanner sc = new Scanner(System.in);
    int optSubj;
    do{
      System.out.printf("Choose the subject from list below to %s of %s:\n\n",option, student.getFullName());
      // Declaring list of subject to hold subjects temporarily
      List<Subject> listOfsubj = new LinkedList<>();
      for (Student stud : students) {
        // If student in list matches the student passed in argument then display subject enrolled by that student
        if(stud.getStudentID()== student.getStudentID()){
          int i = 1; // Variable to display the subject choice count
          for (Subject subject : stud.getSubject()) {
              System.out.println(i+". "+subject.getName());
              // storing subjects in list 
              listOfsubj.add(subject);
              i++;
          }
        }
      }

      System.out.println("\nSelect subject or press 8 to go back or press 0 to exit:");
      optSubj = sc.nextInt();
      
      /* If user choose between 1 to 5 choices or subjects, 
      then either call display grade method
      or set grade method depending on option set
      */ 
      if (optSubj > 0 && optSubj <= 5) {
          // If option is view grade, then call view grade method
          if(option.equals("view grade")){
            viewGrade(student, listOfsubj.get(optSubj-1), outObj, inObj);
          }
          // If option is set grade, then call set grade method
          else if(option.equals("set grade")){
            setGrade(student, listOfsubj.get(optSubj-1), outObj, inObj);
          }
      } 
      // Exis system if 0 is pressed
      else if (optSubj == 0) {
          System.exit(0);
      }
      // Go back to previous menu if 8 is pressed
      else if (optSubj == 8) {
          break;
      } 
      // Display invalid message for any other choices
      else {
          System.out.println("Sorry, invalid option!");
      }

    }while(optSubj!=0);
    
  }

  // Method to view grade of student 
  private static void viewGrade(Student student, Subject subject, ObjectOutputStream outObj, ObjectInputStream inObj){            
    // Create Hashmap to send request to server
    HashMap<String, String> grdRequest = new HashMap<String, String>();
    LinkedList<GradedAssessment> stdGrd;
    
    System.out.printf("Assessment grade for %s in %s: \n\n",student.getFullName(),subject.getName());
    try {
      // Store type of request along with other data in hashmap and send to server
      grdRequest.put("type","view-student-grade-request");
      grdRequest.put("studentID",Integer.toString(student.getStudentID()));
      grdRequest.put("subjectID",Integer.toString(subject.getId()));
      outObj.writeObject(grdRequest);

      // store the response received from server
      stdGrd = (LinkedList) inObj.readObject();

      // Check if size of (grade)response is greater than 0
      if(stdGrd.size() > 0){
        // Display grade for the assessment of that subject
        for(Assessment assmnt: subject.getAssessment()){
          String grade = "Not graded";
          for(GradedAssessment grdAssmnt: stdGrd){
            if(assmnt.getAssessmentID().equals(grdAssmnt.getAssessmentID())){
              grade = grdAssmnt.getGrade().getAchievement();
            }
          }
          System.out.printf("Assessment %s (%s): %s\n",assmnt.getAssessmentID(), assmnt.getType(), grade);
        }
      }
      // If the size of response is less than 0, then no grades set
      else{
        System.out.printf("Sorry, grades for %s is not set yet.\n", subject.getName());
      }
      System.out.println();
    }
    catch (IOException e){
      System.out.println("readline:"+e.getMessage());
    }
    catch(ClassNotFoundException ex){
      ex.printStackTrace();
    }
  }

  // Method to set grade of student 
  private static void setGrade(Student student, Subject subject, ObjectOutputStream outObj, ObjectInputStream inObj){
    Scanner sc = new Scanner(System.in);
    int optAsmnt;
    int optGrd;
    do{
      System.out.printf("Choose the assessment to set grade for %s in %s:\n\n",student.getFullName(),subject.getName());
      String[] asmntID = new String[4];
      for (Student stud : students) {
        // Check if current student id is equal to student id in list
        if(stud.getStudentID() == student.getStudentID()){
          // Access subjects enrolled by that student 
          for (Subject subj : stud.getSubject()) {
            if(subj.getName().equals(subject.getName())){
              int i = 1;
              // Display the list of assessment to set grades for
              for(Assessment asmnt: subj.getAssessment()){
                System.out.printf("%d. Assessment %s (%s)\n",i, asmnt.getAssessmentID(),asmnt.getType());
                asmntID[i] = asmnt.getAssessmentID();
                i++;
              }
            }
          }
        }
      }

      System.out.println("\nSelect assessment or press 8 to go back or press 0 to exit:");
      optAsmnt = sc.nextInt();
      
      // Check if user select valid assessment from the list
      if (optAsmnt > 0 && optAsmnt <= 3) {
        try {
            while(true){
              // Create Hashmap to send request to server
              HashMap<String, String> grdRequest = new HashMap<String, String>();
              // Request grade list from server
              grdRequest.put("type","grade-list-request");
              outObj.writeObject(grdRequest);
              System.out.printf("Choose the grade from below list to set grade for %s in %s (%s):\n\n",student.getFullName(),subject.getName(),asmntID[optAsmnt]);
              
              //Store the response received from server
              grdList = (GradeList) inObj.readObject();
              grades = grdList.getGrade();
              String[] gradesVals = new String[grades.size()+1];
              int i = 1;
              // Display the grade list to user
              for(Grade grd: grades){
                System.out.println(i+". "+grd.getAchievement());
                gradesVals[i] = grd.getAchievement();
                i++;
              }
              System.out.println("\nSelect grade or press 8 to go back or press 0 to exit:");
              optGrd = sc.nextInt();

              // Check if valid grade list is selected
              if (optGrd > 0 && optGrd <= 5) {
                // Sending set grade request to server
                HashMap<String, String> setGrdrequest = new HashMap<String, String>();
                HashMap<String, String> response;
                // Send grades and other information to server to set grade
                setGrdrequest.put("type","set-grade-request");
                setGrdrequest.put("studentID",Integer.toString(student.getStudentID()));
                setGrdrequest.put("subjectID",Integer.toString(subject.getId()));
                setGrdrequest.put("assessmentID",asmntID[optAsmnt]);
                setGrdrequest.put("gradeID",Integer.toString(optGrd));
                outObj.writeObject(setGrdrequest);

                // Store response send by server
                response = (HashMap) inObj.readObject();
                // If success status is received then display success message
                if(response.get("status").equals("success")){
                  System.out.printf("Grade \"%s\" successfully set for %s in %s (%s) \n\n",gradesVals[optGrd], student.getFullName(), subject.getName(), asmntID[optAsmnt]);
                }
                optGrd = 8;
              }
              // System exit
              else if (optGrd == 0) {
                  System.exit(0);
              }
              else {
                  System.out.println("Sorry, invalid option!");
              }

              // Go back to previous menu if 8 is pressed
              if (optGrd == 8) {
                  break;
              } 
            }
            
        }
        // Catch exceptions
        catch(ArrayIndexOutOfBoundsException exception) {
            System.out.println("Sorry, invalid option!");
        }
        catch (IOException e){
          System.out.println("readline:"+e.getMessage());
        }
        catch(ClassNotFoundException ex){
           ex.printStackTrace();
        }
          
      } 
      // System exit
      else if (optAsmnt == 0) {
          System.exit(0);
      }
      // Go back to previous menu if 8 is pressed
      else if (optAsmnt == 8) {
          break;
      } 
      // Display invalid message
      else {
          System.out.println("Sorry, invalid option!");
      }
    }while(optAsmnt!=0);
    
      
  }

  /**
   *  encrypt
   * @param message
   * @return
   * @throws Exception
   */
  public static byte [] encrypt( String message) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte [] cipherData = cipher.doFinal(message.getBytes("UTF-8"));
    return cipherData;
  }

  public static void main (String args[]) {
    
    Socket s = null;
    byte []  bytesPublicKey = null;

    try{
      // Initilizing port to communicate with server
      int serverPort = 8888;
      // Creating socket to communicate with server
      s = new Socket("localhost", serverPort);    
      
      // Declaring and initializing Object stream 
      ObjectOutputStream outObj = new ObjectOutputStream( s.getOutputStream());
      ObjectInputStream inObj  =new ObjectInputStream(s.getInputStream());
      DataInputStream inData = new DataInputStream(s.getInputStream());
      DataOutputStream outData =new DataOutputStream(s.getOutputStream());
      HashMap<String, String> request = new HashMap<String, String>();
      HashMap<String, String> response = new HashMap<String, String>();

      Scanner sc = new Scanner(System.in);
      int userType = 0;

      do{
        // Initilizing values to display to user
        String optOne = "1. List of assessments for a chosen subject";
        String optTwo = "2. Grades of assessments for a student";
        String optThree = "3. Set grade for a chosen student and subject";

        String viewAsses = "Choose the subject from below list to view assessment:\n";
        String viewGrd = "Choose the Student from below list to view assessment grades:\n";
        String setGrd = "Choose the Student from below list to set grades:\n";
        int optInput;
        int optSubj;
        int optStud;
        String user;
        String msgServer;

        // Welcome message to user
        System.out.println("\n\nWelcome to Study Progress Monitor System");

        do {
          // sent hello to server and get menu
          request = new HashMap<String, String>();
          request.put("type", "hello");
          outObj.writeObject(request);
          // display menu
          System.out.println("\n" + inData.readUTF());

          // register or login
          String LoginType = sc.nextLine();
          request = new HashMap<String, String>();
          request.put("type", "hello");
          request.put("LoginType",LoginType);
          outObj.writeObject(request);
          System.out.println(inData.readUTF());

          // user Type
          String inputUserType = sc.nextLine();
          request = new HashMap<String, String>();
          request.put("type", "hello");
          request.put("LoginType",LoginType);
          request.put("userType",inputUserType);
          outObj.writeObject(request);
          System.out.print(inData.readUTF()+"\n");

          switch(inputUserType){
            case 1:

          }
          // user name
          int pubKeyLength = 0;
          String inputUserName;
          while (pubKeyLength == 0) {
            inputUserName = sc.nextLine();
            request = new HashMap<String, String>();
            request.put("type", "hello");
            request.put("LoginType",LoginType);
            request.put("userType",inputUserType);
            request.put("UserName",inputUserName);
            outObj.writeObject(request);
            System.out.println(inData.readUTF());
            pubKeyLength = inData.readInt();
          }
          if(publicKey == null && pubKeyLength > 0) {
            //read the size PublicKey
            bytesPublicKey = new byte[pubKeyLength];
            //read the PublicKey in bytes sent from the sever
            inData.readFully(bytesPublicKey, 0, pubKeyLength);
            //generate the key speciifcation for encoding
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(bytesPublicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            //extract the PublicKey
            publicKey = keyFactory.generatePublic(pubKeySpec);
          }

          //ecrypt the password
          byte[] encodedmessage =  encrypt(sc.nextLine());
          request = new HashMap<String, String>();
          request.put("type","register");
          outObj.writeObject(request);
          //send the encrypted password length
          outData.writeInt(encodedmessage.length);
          //send the encrypted password in bytes
          outData.write(encodedmessage, 0, encodedmessage.length);

          System.out.println(inData.readUTF());
        } while (userType==0);

        userType = sc.nextInt();
        // Set user string depending upon user type
        user = (userType == 1 )? "Administrator" : "Parents/Students";
        // Display the menu until user choose option
        do{
               System.out.printf("\nWelcome %s \n",user); // Welcome message for user

                // Display view assessment and view grades menu to all users
                System.out.println("\n"+optOne);
                System.out.println(optTwo);
                
                // Display set grade menu only to Administrator
                if(userType == 1){
                  System.out.println(optThree);
                }

                // prompt for user input
                System.out.println("\nSelect your option or press 0 to exit:");
                optInput = sc.nextInt();

                // Perform operation based on user input
                switch(optInput){
                  // View assessment
                  case 1:
                      do{
                        // Create Hashmap to send request to server
                        request = new HashMap<String, String>();
                        System.out.println(viewAsses);
                        // Store type of request in hashmap and send to server
                        request.put("type","view-assessment-request");
                        outObj.writeObject(request);

                        // store the response received from server
                        subjList = (SubjectList) inObj.readObject();  
                        subjects = subjList.getSubject();
                        String[] subjNames = new String[subjects.size()+1];
                        int i = 1;
                        // Display list of subjects received from server
                        for (Subject subj : subjects) {
                            System.out.println(i+". "+subj.getName());
                            subjNames[i] = subj.getName();
                            i++;
                        }
                        System.out.println("\nSelect subject or press 8 to go back or press 0 to exit:");
                        optSubj = sc.nextInt();                        

                        // If user select valid option then display assessment 
                        if(optSubj >0 && optSubj <=5){
                          displayAssessment(subjNames[optSubj]);
                        }
                        // Exit application if 0 is pressed
                        else if(optSubj == 0){
                          System.exit(0);
                        }
                        // Go back to previous menu if 8 is pressed
                        else if(optSubj == 8){
                          break;
                        }
                        // Display invalid option message
                        else{
                          System.out.println("Sorry, invalid option!");
                        }

                      }while(optSubj != 0);
                      
                      break;
                  case 2:
                    // View grades
                      do{
                        // Create Hashmap to send request to server
                        request = new HashMap<String, String>();
                        System.out.println(viewGrd);

                        // Store type of request in hashmap and send to server
                        request.put("type","student-list-request");
                        outObj.writeObject(request);

                        // store the response received from server
                        studList = (StudentList) inObj.readObject();  
                        students = studList.getStudent();

                        List<Student> listOfstud = new LinkedList<>();
                        int i = 1;
                        // Display list of students received from server
                        for (Student stud : students) {
                            System.out.println(i+". "+stud.getFullName());
                            listOfstud.add(stud);
                            i++;
                        }
                        System.out.println("\nSelect student or press 8 to go back or press 0 to exit:");
                        optStud = sc.nextInt();
                        
                        // If user select valid option then display subjects enrolled by that student 
                        if(optStud >0 && optStud <=5){
                          displaySubject(listOfstud.get(optStud-1),"view grade", outObj, inObj);
                        }
                        // Exit application if 0 is pressed
                        else if(optStud == 0){
                          System.exit(0);
                        }
                        // Go back to previous menu if 8 is pressed
                        else if(optStud == 8){
                          break;
                        }
                        // Display invalid option message
                        else{
                          System.out.println("Sorry, invalid option!");
                        }

                      }while(optStud != 0);

                      break;
                  case 3:
                      //Set grades
                      if(userType == 1){ // Set grade is accessed by Administrator only
                        do{
                          // Create Hashmap to send request to server
                          request = new HashMap<String, String>();
                          System.out.println(setGrd);

                          // Store type of request in hashmap and send to server
                          request.put("type","student-list-request");
                          outObj.writeObject(request);

                          // store the response received from server
                          studList = (StudentList) inObj.readObject();  
                          students = studList.getStudent();

                          List<Student> listOfstud = new LinkedList<>();
                          int i = 1;
                          // Display list of students received from server
                          for (Student stud : students) {
                              System.out.println(i+". "+stud.getFullName());
                              listOfstud.add(stud);
                              i++;
                          }
                          System.out.println("\nSelect student or press 8 to go back or press 0 to exit:");
                          optStud = sc.nextInt();

                          // If user select valid option then display subjects enrolled by that student 
                          if(optStud >0 && optStud <=5){
                            displaySubject(listOfstud.get(optStud-1),"set grade",outObj, inObj);
                          }
                          // Exit application if 0 is pressed
                          else if(optStud == 0){
                            System.exit(0);
                          }
                          // Go back to previous menu if 8 is pressed
                          else if(optStud == 8){
                            break;
                          }
                          // Display invalid option message
                          else{
                            System.out.println("Sorry, invalid option!");
                          }

                        }while(optStud != 0);
                      }
                      else{
                        System.out.println("Sorry, invalid option!");
                      }
                    
                      break;
                   // Exit application if 0 is pressed
                  case 0:
                    System.exit(0);
                }
              }while(optInput !=0);

      }while(userType!=0);

    }
    // Catch exceptions
    catch (UnknownHostException e){
      System.out.println("Socket:"+e.getMessage());
    }
    catch (EOFException e){
      System.out.println("EOF:"+e.getMessage());
    }
    catch (IOException e){
      System.out.println("readline:"+e.getMessage());
    }
    catch (NoSuchAlgorithmException e){
      System.out.println("Algorithm: "+ e.getMessage());
    }
    catch(ClassNotFoundException ex){
           ex.printStackTrace();
    }
    catch (InvalidKeySpecException e){
      System.out.println("invalid key: "+ e.getMessage());
    }
    catch (Exception e) {
      System.out.println("Exception: "+e.getMessage());
    }
    finally {
      if(s!=null) 
        try {
          s.close();
        }
        catch (IOException e){
          System.out.println("close:"+e.getMessage());
        }
      }
     }
}
