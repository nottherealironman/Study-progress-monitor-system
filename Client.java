import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;

public class Client {
  private static SubjectList subjList;
  private static StudentList studList;
  private static LinkedList<Subject> subjects;

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
  
  public static void main (String args[]) {
    
    Socket s = null;
    
    try{
      int serverPort = 8888;
      
      s = new Socket("localhost", serverPort);    
      
      ObjectInputStream inObj = null;
      ObjectOutputStream outObj =null;
      
      outObj =new ObjectOutputStream(s.getOutputStream());
      inObj = new ObjectInputStream( s.getInputStream());

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
                  case 1:
                      do{
                        System.out.println(viewAsses);
                        outData.writeUTF("Subject List");
                        subjList = (SubjectList) inObj.readObject();  
                        //getClass().getName()
                        //System.out.println("subjList : " + subjList.getSubject());
                        subjects = subjList.getSubject();
                        int i = 1;
                        for (Subject subj : subjects) {
                            System.out.println(i+". "+subj.getName());
                            i++;
                        }
                        System.out.println("\nSelect subject or press 8 to go back or press 0 to exit:");
                        optSubj = sc.nextInt();

                        switch(optSubj){
                          case 1:
                              displayAssessment("English");
                              break;

                          case 2:
                              displayAssessment("Maths B");
                              break;
                          case 3:
                              displayAssessment("Biology");
                              break;
                          case 4:
                              displayAssessment("Business and Communication Technologies");
                              break;
                          case 5:
                              displayAssessment("Religion and Ethics");
                              break;
                          case 0:
                              System.exit(0);
                        }
                        if(optSubj == 8){
                          break;
                        }

                      }while(optSubj != 0);
                      
                      break;
                  case 2:
                      System.out.println(viewGrd);
                      outData.writeUTF("Student List");
                      studList = (StudentList) inObj.readObject();  
                      //System.out.println("====================================");
                      System.out.println("studList : " + studList);
                      break;
                  case 3:
                      System.out.println(setGrd);
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
