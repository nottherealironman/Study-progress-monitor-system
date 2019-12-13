import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client {

  
  public static void main (String args[]) {
    
    Socket s = null;
    
    try{
      
      int serverPort = 8888;
      
      s = new Socket("localhost", serverPort);    
      
      /*ObjectInputStream in = null;
      ObjectOutputStream out =null;
      */
      /*ObjectInputStream inObj = new ObjectInputStream(s.getInputStream());
      ObjectOutputStream outObj =new ObjectOutputStream(s.getOutputStream());*/

      DataInputStream inData = new DataInputStream(s.getInputStream());
      DataOutputStream outData = new DataOutputStream(s.getOutputStream());
      System.out.println("HW");
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
        int optInput;

        switch(userType){
            // Administrator 
            case 1:
                System.out.println("\nWelcome Administrator");
                System.out.println("\n"+optOne);
                System.out.println(optTwo);
                System.out.println(optThree);
                System.out.println("\nSelect your option or press 0 to exit:");

                optInput = sc.nextInt();

                switch(optInput){
                  case 1:
                      System.out.println(viewAsses);
                      outData.writeUTF(subjListRequest);
                      break;
                  case 2:
                      System.out.println(viewGrd);
                      break;
                  case 3:
                      System.out.println(setGrd);
                      break;
                  case 0:
                    System.exit(0);
                }
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

        /*while(true){
          
            

          }*/
      }while(userType!=0);

      /*Book book = new Book();
      book.setTitle(new String("Network Security"));
      book.setAuthor(new String("Mark Ciampa"));
      book.setPublisher(new String("Thomson Course Technology"));
      book.setYear(new String("2005"));
      book.setISBN(new String("0-619-21566-6"));
      out.writeObject(book); 
      
      System.out.println("The Sent Book Object:");
      System.out.println("====================================");
      System.out.println("Book Title: " + book.getTitle());
      System.out.println("Book Author: " + book.getAuthor());
      System.out.println("Publish Year: " + book.getYear());
      System.out.println("ISBN: " + book.getISBN());
      System.out.println();
      
      Book book1 = (Book)in.readObject();     
      System.out.println("The Received Book Object:");
      System.out.println("====================================");
      System.out.println("Book Title: " + book1.getTitle());
      System.out.println("Book Author: " + book1.getAuthor());
      System.out.println("Publish Year: " + book1.getYear());
      System.out.println("ISBN: " + book1.getISBN()); */
      
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
    /*catch(ClassNotFoundException ex){
           ex.printStackTrace();
    }*/
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
