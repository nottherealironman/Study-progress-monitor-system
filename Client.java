import java.net.*;
import java.io.*;
public class Client {
  public static void main (String args[]) {
    
    Socket s = null;
    
    try{
      
      int serverPort = 8888;
      
      s = new Socket("localhost", serverPort);    
      
      ObjectInputStream in = null;
      ObjectOutputStream out =null;
      
      out =new ObjectOutputStream(s.getOutputStream());
      in = new ObjectInputStream( s.getInputStream());

      Book book = new Book();
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
      System.out.println("ISBN: " + book1.getISBN()); 
      
    }catch (UnknownHostException e){System.out.println("Socket:"+e.getMessage());
    }catch (EOFException e){System.out.println("EOF:"+e.getMessage());
    }catch (IOException e){System.out.println("readline:"+e.getMessage());
    }catch(ClassNotFoundException ex){
           ex.printStackTrace();
    }finally {if(s!=null) try {s.close();}catch (IOException e){System.out.println("close:"+e.getMessage());}}
     }
}
