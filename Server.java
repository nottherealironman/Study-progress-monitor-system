import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {
	public static void main (String args[]) {
		
		try{
			int serverPort = 8888; 
			ServerSocket listenSocket = new ServerSocket(serverPort);
			
			while(true) {
				Socket clientSocket = listenSocket.accept();
				Connection c = new Connection(clientSocket);
			}

		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
}
class Connection extends Thread {
	
	ObjectInputStream inObj;
	ObjectOutputStream outObj;
	DataInputStream inData;
	DataOutputStream outData;
	Socket s;
	
	public Connection (Socket aClientSocket) {
		
		try {
			s = aClientSocket;
			/*in = new ObjectInputStream( clientSocket.getInputStream());
			out =new ObjectOutputStream( clientSocket.getOutputStream());*/
			inObj = new ObjectInputStream(s.getInputStream());
	        outObj =new ObjectOutputStream(s.getOutputStream());

	        inData = new DataInputStream(s.getInputStream());
	        outData = new DataOutputStream(s.getOutputStream());
			/*in = new DataInputStream(clientSocket.getInputStream());
	      	out = new DataOutputStream(clientSocket.getOutputStream());*/
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}

	public void run(){
		
		try {			                 
			/*String request = inData.readUTF();
			System.out.println("Request : "+request);*/
			DatabaseUtility dataObj = new DatabaseUtility();
	        dataObj.createDBtables();

	        LinkedList<Subject> result = dataObj.fetchSubjectList();
	        //out.writeUTF("Server received:"+result);
	        //LinkedList<Assessment> result = dataObj.fetchAssessmentList(1);
	        SubjectList subjList = new SubjectList(result);
	        /*for(Subject res: result){
	            subjList = (res.getName());
	        }*/
	        outObj.writeObject(subjList);
			/*switch(request){
				case "Subject List":

			}*/
			/*Book book1 = (Book)inObj.readObject();	
			
			System.out.println("The Received Book Object:");
			System.out.println("====================================");
			System.out.println("Book Title: " + book1.getTitle());
			System.out.println("Book Author: " + book1.getAuthor());
			System.out.println("Publish Year: " + book1.getYear());
			System.out.println("ISBN: " + book1.getISBN());	
			System.out.println();

			Book book = new Book();
			book.setTitle(new String("Firewalls and Network Security"));
			book.setAuthor(new String("Greg Holden"));
			book.setPublisher(new String("Thomson Course Technology"));
			book.setYear(new String("2005"));
			book.setISBN(new String("0-619-13039-3"));
			outObj.writeObject(book);
			
			System.out.println("The Sent Book Object:");
			System.out.println("====================================");
			System.out.println("Book Title: " + book.getTitle());
			System.out.println("Book Author: " + book.getAuthor());
			System.out.println("Publish Year: " + book.getYear());
			System.out.println("ISBN: " + book.getISBN());	*/
			

			/*DatabaseUtility dataObj = new DatabaseUtility();
	        //dataObj = new DatabaseUtility();
	        dataObj.createDBtables();
	        ArrayList<String> result = dataObj.fetchAssessmentList(1);
	        out.writeUTF("Server received:"+result);
	        for(String res: result){
	            System.out.println(res);
	        }*/


		}
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage());
		} 
		catch(IOException e) {
			System.out.println("readline:"+e.getMessage());
		} 
		/*catch(ClassNotFoundException ex){
					 ex.printStackTrace();
		}*/
		finally{ 
			try {
			s.close();
			}
			catch (IOException e){
			/*close failed*/}
		}
		

	}
}
