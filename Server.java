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
	DatabaseUtility dataObj = new DatabaseUtility();
	LinkedList<Subject> subjResult;
	LinkedList<Student> studResult;
	SubjectList subjList;
	StudentList studList;
	String request;
	
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
			
			System.out.println("Request : "+request);
			
	        dataObj.createDBtables();

	        while ((request= inData.readUTF())!=null){
				switch(request){
					case "view-assessment-request":
						//System.out.println("Subject List called");
						subjResult = dataObj.fetchSubjectList();
	        			subjList = new SubjectList(subjResult);
				        outObj.writeObject(subjList);
				        break;
				    case "view-grade-request":
				    	studResult = dataObj.fetchStudentList();
						studList = new StudentList(studResult);
				    	outObj.writeObject(studList);
				    	break;

				    case "set-grade-request":
						studResult = dataObj.fetchStudentList();
						studList = new StudentList(studResult);
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
