import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class Client {
  private static SubjectList subjList;
  private static StudentList studList;
  private static GradeList grdList;
  private static LinkedList<Subject> subjects;
  private static LinkedList<Student> students;
  private static LinkedList<Grade> grades;

  private static void displayAssessment(String subject){
    System.out.printf("Assignment of %s:\n\n",subject);
   
    for (Subject subj : subjects) {
      if(subj.getName().equals(subject)){
        for (Assessment assessment : subj.getAssessment()) {
          System.out.println(assessment);
        }
      }
    }
  }

  private static void displaySubject(String student, String option, DataOutputStream outData, ObjectInputStream inObj){
    Scanner sc = new Scanner(System.in);
    int optSubj;
    do{
      System.out.printf("Choose the subject from list below to %s of %s:\n\n",option, student);
      String[] subjNames = new String[6];
      for (Student stud : students) {
        //subjNames = new String[stud.getSubject().size()+1];
        if(stud.getFullName().equals(student)){
          int i = 1;
          for (Subject subject : stud.getSubject()) {
              System.out.println(i+". "+subject.getName());
              subjNames[i] = subject.getName();
              i++;
          }
        }
      }

      System.out.println("\nSelect subject or press 8 to go back or press 0 to exit:");
      optSubj = sc.nextInt();
      
      if (optSubj > 0 && optSubj <= 5) {
          if(option.equals("view grade")){
            viewGrade(student,subjNames[optSubj], outData, inObj);
          }
          else if(option.equals("set grade")){
            setGrade(student,subjNames[optSubj], outData, inObj);
          }
      } 
      // System exit
      else if (optSubj == 0) {
          System.exit(0);
      }
      // Go back to previous menu if 8 is pressed
      else if (optSubj == 8) {
          break;
      } 
      else {
          System.out.println("Sorry, invalid option!");
      }

    }while(optSubj!=0);
    
  }

  private static void viewGrade(String student, String subject, DataOutputStream outData, ObjectInputStream inObj){            
    /*switch(optSubj){

    }*/
    System.out.printf("view grade for %s in %s",student,subject);
  }

  private static void setGrade(String student, String subject, DataOutputStream outData, ObjectInputStream inObj){
    Scanner sc = new Scanner(System.in);
    int optAsmnt;
    int optGrd;
    do{
      System.out.printf("Choose the assessment to set grade for %s in %s:\n\n",student,subject);
      String[] asmntID = new String[4];
      for (Student stud : students) {
        //subjNames = new String[stud.getSubject().size()+1];
        if(stud.getFullName().equals(student)){
          
          for (Subject subj : stud.getSubject()) {
            if(subj.getName().equals(subject)){
              int i = 1;
              for(Assessment asmnt: subj.getAssessment()){
                System.out.printf("%d. Assessment %s (%s)\n",i, asmnt.getAssessmentID(),asmnt.getType());
                asmntID[i] = asmnt.getAssessmentID();
                i++;
              }
              //System.out.println();
            }
          }
        }
      }

      System.out.println("\nSelect assessment or press 8 to go back or press 0 to exit:");
      optAsmnt = sc.nextInt();
      
      if (optAsmnt > 0 && optAsmnt <= 3) {
        try {
            while(true){
              //String[] grdID = new String[6];
              //asmntID[optAsmnt]
              System.out.printf("Choose the grade from below list to set grade for %s in %s (%s):\n\n",student,subject,asmntID[optAsmnt]);
              outData.writeUTF("grade-list-request");
              grdList = (GradeList) inObj.readObject();
              grades = grdList.getGrade();
              int i = 1;
              for(Grade grd: grades){
                System.out.println(i+". "+grd.getAchievement());
                i++;
              }
              System.out.println("\nSelect grade or press 8 to go back or press 0 to exit:");
              optGrd = sc.nextInt();

              if (optGrd > 0 && optGrd <= 5) {
                outData.writeUTF("@set-grade-request@"+student+"&"+subject+"&"+asmntID[optAsmnt]+"&"+optGrd);
              }
              // System exit
              else if (optGrd == 0) {
                  System.exit(0);
              }
              // Go back to previous menu if 8 is pressed
              else if (optGrd == 8) {
                  break;
              } 
              else {
                  System.out.println("Sorry, invalid option!");
              }
            }
            
        }
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
      else {
          System.out.println("Sorry, invalid option!");
      }
    }while(optAsmnt!=0);
    
      
  }
  
  public static void main (String args[]) {
    
    Socket s = null;
    
    try{
      int serverPort = 8888;
      
      s = new Socket("localhost", serverPort);    
      
      ObjectOutputStream outObj = new ObjectOutputStream( s.getOutputStream());;
      ObjectInputStream inObj  =new ObjectInputStream(s.getInputStream());;

      DataInputStream inData = new DataInputStream(s.getInputStream());
      DataOutputStream outData = new DataOutputStream(s.getOutputStream());

      Scanner sc = new Scanner(System.in);
      int userType;

      do{
        System.out.println("\n\nWelcome to Study Progress Monitor System\n");
        System.out.println("1. Administrator");
        System.out.println("2. Guest");
        System.out.println("\nSelect user type or press 0 to exit:");
        userType = sc.nextInt();

        String optOne = "1. List of assessments for a chosen subject";
        String optTwo = "2. Grades of assessments for a student";
        String optThree = "3. Set grade for a chosen student and subject";

        String viewAsses = "Choose the subject from below list to view assessment:\n";
        String viewGrd = "Choose the Student from below list to view assessment grades:\n";
        String setGrd = "Choose the Student from below list to set grades:\n";
        String subjListRequest = "Subject List";
        String studListRequest = "Student List";
        int optInput;
        int optSubj;
        int optStud;

        switch(userType){
            // Administrator 
            case 1:
              System.out.println("\nWelcome Administrator");
              do{
                
                System.out.println("\n"+optOne);
                System.out.println(optTwo);
                System.out.println(optThree);
                System.out.println("\nSelect your option or press 0 to exit:");

                optInput = sc.nextInt();
                
                switch(optInput){
                  // View assessment
                  case 1:
                      do{
                        System.out.println(viewAsses);
                        outData.writeUTF("view-assessment-request");
                        subjList = (SubjectList) inObj.readObject();  
                        //getClass().getName()
                        //System.out.println("subjList : " + subjList.getSubject());
                        subjects = subjList.getSubject();
                        String[] subjNames = new String[subjects.size()+1];
                        int i = 1;
                        for (Subject subj : subjects) {
                            System.out.println(i+". "+subj.getName());
                            subjNames[i] = subj.getName();
                            i++;
                        }
                        System.out.println("\nSelect subject or press 8 to go back or press 0 to exit:");
                        optSubj = sc.nextInt();

                        switch(optSubj){
                          case 1:
                              displayAssessment(subjNames[optSubj]);
                              break;
                          case 2:
                              displayAssessment(subjNames[optSubj]);
                              break;
                          case 3:
                              displayAssessment(subjNames[optSubj]);
                              break;
                          case 4:
                              displayAssessment(subjNames[optSubj]);
                              break;
                          case 5:
                              displayAssessment(subjNames[optSubj]);
                              break;
                          case 0:
                              System.exit(0);
                        }
                        // Go back to previous menu if 8 is pressed
                        if(optSubj == 8){
                          break;
                        }

                      }while(optSubj != 0);
                      
                      break;
                  case 2:
                    // View grades
                      do{
                        System.out.println(viewGrd);
                        outData.writeUTF("student-list-request");
                        studList = (StudentList) inObj.readObject();  
                        //System.out.println("====================================");
                        //System.out.println("studList : " + studList);

                        students = studList.getStudent();
                        String[] studNames = new String[students.size()+1];
                        int i = 1;
                        for (Student stud : students) {
                            System.out.println(i+". "+stud.getFullName());
                            studNames[i] = stud.getFullName();
                            i++;
                        }
                        System.out.println("\nSelect student or press 8 to go back or press 0 to exit:");
                        optStud = sc.nextInt();

                        

                        /*switch(optStud){
                          case 1:
                              displaySubject(studNames[optStud]);
                              break;

                          case 2:
                              displaySubject(studNames[optStud]);
                              break;
                          case 3:
                              displaySubject(studNames[optStud]);
                              break;
                          case 4:
                              displaySubject(studNames[optStud]);
                              break;
                          case 5:
                              displaySubject(studNames[optStud]);
                              break;
                          case 0:
                              System.exit(0);
                        }*/
                        if(optStud >0 && optStud <=5){
                          displaySubject(studNames[optStud],"view grade", outData, inObj);
                        }
                        else if(optStud == 0){
                          System.exit(0);
                        }
                        // Go back to previous menu if 8 is pressed
                        else if(optStud == 8){
                          break;
                        }
                        else{
                          System.out.println("Sorry, invalid option!");
                        }

                      }while(optStud != 0);

                      break;
                  case 3:
                      //Set grades
                      do{
                        System.out.println(setGrd);
                        outData.writeUTF("student-list-request");
                        studList = (StudentList) inObj.readObject();  
                        //System.out.println("====================================");
                        //System.out.println("studList : " + studList);

                        students = studList.getStudent();
                        String[] studNames = new String[students.size()+1];
                        int i = 1;
                        for (Student stud : students) {
                            System.out.println(i+". "+stud.getFullName());
                            studNames[i] = stud.getFullName();
                            i++;
                        }
                        System.out.println("\nSelect student or press 8 to go back or press 0 to exit:");
                        optStud = sc.nextInt();
                        
                        /*switch(optStud){
                          case 1:
                              displaySubject(studNames[optStud]);
                              break;

                          case 2:
                              displaySubject(studNames[optStud]);
                              break;
                          case 3:
                              displaySubject(studNames[optStud]);
                              break;
                          case 4:
                              displaySubject(studNames[optStud]);
                              break;
                          case 5:
                              displaySubject(studNames[optStud]);
                              break;
                          case 0:
                              System.exit(0);
                        }*/

                        if(optStud >0 && optStud <=5){
                          displaySubject(studNames[optStud],"set grade",outData, inObj);
                        }
                        else if(optStud == 0){
                          System.exit(0);
                        }
                        // Go back to previous menu if 8 is pressed
                        else if(optStud == 8){
                          break;
                        }
                        else{
                          System.out.println("Sorry, invalid option!");
                        }

                      }while(optStud != 0);

                      break;
                  case 0:
                    System.exit(0);
                }
              }while(optInput !=0);
            
                
                break;

            // Guest
            case 2:
                System.out.println("\nWelcome Guest");
                System.out.println("\n"+optOne);
                System.out.println(optTwo);
                System.out.println("\nSelect your option or press 0 to exit:");
                optInput = sc.nextInt();
                switch(optInput){
                  case 1:
                      System.out.println(viewAsses);
                      break;
                  case 2:
                      System.out.println(viewGrd);
                      break;
                  case 0:
                    System.exit(0);
                }
                break;
            }

      }while(userType!=0);

    }
    catch (UnknownHostException e){
      System.out.println("Socket:"+e.getMessage());
    }
    catch (EOFException e){
      System.out.println("EOF:"+e.getMessage());
    }
    catch (IOException e){
      System.out.println("readline:"+e.getMessage());
    }
    catch(ClassNotFoundException ex){
           ex.printStackTrace();
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
