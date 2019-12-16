import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

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
	LinkedList<Grade> grdResult;
	SubjectList subjList;
	StudentList studList;
	GradeList grdList;
	String request;
	String orgRequest;
	
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
	        dataObj.createDBtables();

	        while ((request= inData.readUTF())!=null){
	        	//System.out.println("Request : "+request);
	        	if(request.startsWith("@")){
	        		orgRequest = request;
	        		System.out.printf("Found : %s\n",request.substring(1, request.indexOf("@", request.indexOf("@")+1)));
	        		System.out.println("Request : "+request);
	        		request = request.substring(1, request.indexOf("@", request.indexOf("@")+1));
	        	}
				switch(request){
					case "view-assessment-request":
						//System.out.println("Subject List called");
						subjResult = dataObj.fetchSubjectList();
	        			subjList = new SubjectList(subjResult);
				        outObj.writeObject(subjList);
				        break;
				    case "student-list-request":
				    	studResult = dataObj.fetchStudentList();
						studList = new StudentList(studResult);
				    	outObj.writeObject(studList);
				    	break;

				    case "set-grade-request":
				    System.out.println(orgRequest.lastIndexOf("set-grade-request"));
				    	String sub = orgRequest.substring(19,orgRequest.length());
				    	String[] data = sub.split("&");
				    	System.out.println(data[0]+ data[1] +data[2]+Integer.parseInt(data[3]));
				    	dataObj.insertStudentGrade(data[0], data[1] ,data[2],Integer.parseInt(data[3]));
						/*studResult = dataObj.fetchStudentList();
						studList = new StudentList(studResult);
				    	outObj.writeObject(studList);*/
				        break;

				    case "grade-list-request":
						grdResult = dataObj.fetchGradeList();
						grdList = new GradeList(grdResult);
				    	outObj.writeObject(grdList);
				        break;

				    default:
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
