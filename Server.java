import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server {
	private static int clientCount;
	public static void main (String args[]) {
		
		try{
			int serverPort = 8888; 
			ServerSocket listenSocket = new ServerSocket(serverPort);
			int i=0;
			while(true) {
				Socket clientSocket = listenSocket.accept();
				clientCount++;
				Connection c = new Connection(clientSocket, i++,clientCount);
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
	int thrdn;
	int clientCount;
	
	public Connection (Socket aClientSocket, int tn, int client) {
		
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
			String request = inData.readUTF();
			System.out.println("Request : "+request);
			DatabaseUtility dataObj = new DatabaseUtility();
	        dataObj.createDBtables();

	        

	        

	        while (request!=null){
				switch(request){
					case "Subject List":
						System.out.println("Subject List called");
						LinkedList<Subject> subjResult = dataObj.fetchSubjectList();
	        			SubjectList subjList = new SubjectList(subjResult);
				        outObj.writeObject(subjList);
				        break;
				    case "Student List":
				    System.out.println("Student List called");
				    	LinkedList<Student> studResult = dataObj.fetchStudentList();
						StudentList studList = new StudentList(studResult);
				    	outObj.writeObject(studList);
				    	break;
				}
			}

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
