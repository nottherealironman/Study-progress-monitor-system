import java.net.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Server {
	private static int clientCount;
	public static void main (String args[]) {
		
		try{
			// Initilizing server port
			int serverPort = 8888; 
			// Creating socket to communicate with client
			ServerSocket listenSocket = new ServerSocket(serverPort);
			int i=0;
			System.out.println("Server started");
			while(true) {
				// Creating socket to listen to client request
				Socket clientSocket = listenSocket.accept();
				clientCount++;
				// Connection class to create threads to handle multiple clients
				Connection c = new Connection(clientSocket, i++,clientCount);
			}

		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
	}
}
class Connection extends Thread {
	
	// Declaration of classes, variables, list and streams
	ObjectInputStream inObj;
	ObjectOutputStream outObj;
	Socket s;
	int thrdn;
	int clientCount;
	DatabaseUtility dataObj = new DatabaseUtility();
	LinkedList<Subject> subjResult;
	LinkedList<Student> studResult;
	LinkedList<Grade> grdResult;
	LinkedList<GradedAssessment> stdGrd;
	SubjectList subjList;
	StudentList studList;
	GradeList grdList;
	GradedAssessment grdAssmnt;
	HashMap<String, String> request;
	String orgRequest;
	boolean dbStatus;
	String requestType;
	
	public Connection (Socket aClientSocket, int tn, int client) {
		
		try {
			thrdn=tn;
			clientCount =client;
			s = aClientSocket;
			
			// Initializing Input and Output object stream to communicate with clients
			inObj = new ObjectInputStream(s.getInputStream());
	        outObj =new ObjectOutputStream(s.getOutputStream());
			
	      	System.out.println("Client count: "+client);
	      	// Calling start method of thread to handle client request
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}

	public void run(){
		
		try {		
			System.out.printf("Thread %d for Client %d\n",thrdn, clientCount);
			// Method to create and populate tables automatically
	        dataObj.createDBtables();

	        // Parse request from client 
	        while ((request= (HashMap) inObj.readObject())!=null){
	        	// Fetch the type of request send by client
	        	requestType = request.get("type");
	        	// Use switch case statement to perform database query to handle appropriate request
				switch(requestType){
					case "view-assessment-request":
						// calling database method to fetch subjects
						subjResult = dataObj.fetchSubjectList();
						// Storing list of subjects in SubjectList
	        			subjList = new SubjectList(subjResult);
	        			// sending the response to client
				        outObj.writeObject(subjList);
				        break;

				    case "student-list-request":
				    	synchronized(this.getClass()){
				    	// calling database method to fetch students
				    	studResult = dataObj.fetchStudentList();
				    	// Storing list of students in StudentList
						studList = new StudentList(studResult);
						// sending the response to client
				    	outObj.writeObject(studList);
				    	break;
				    	}

				    case "grade-list-request":
				    	// calling database method to fetch grades
						grdResult = dataObj.fetchGradeList();
						// Storing list of grades in GradeList
						grdList = new GradeList(grdResult);
						// sending the response to client
				    	outObj.writeObject(grdList);
				        break;

				    case "view-student-grade-request":
				    	// calling database method to fetch student grade
				    	stdGrd = dataObj.fetchStudentGrade(Integer.parseInt(request.get("studentID")), Integer.parseInt(request.get("subjectID")));
				    	// sending the response to client
				    	outObj.writeObject(stdGrd);
				    	break;

				    case "set-grade-request":
				    	// calling database method to insert student grade in database
						dbStatus = dataObj.insertStudentGrade(Integer.parseInt(request.get("studentID")), Integer.parseInt(request.get("subjectID")), request.get("assessmentID"), Integer.parseInt(request.get("gradeID")));
						// Creating response of HashMap type to sent to client
						HashMap<String, String> response = new HashMap<String, String>();
						// if student grade is inserted successfully the send success status to client else send fail status
						if(dbStatus){
							response.put("status","success");	
						}
						else{
							response.put("status","fail");
						}
						// sending the response to client
						outObj.writeObject(response);
				        break;
				        
				    case "add-student-request":
				    	synchronized(this.getClass()){
					    	// Creating response of HashMap type to send to client
					    	HashMap response1 = new HashMap();
					    	boolean caper = false;
	
					    	// calling database method to fetch students
					    	studResult = dataObj.fetchStudentList();
					    	// Iterate through list of students, verify new student do no exist already ;
					    	
					    	// lambda expression in filter checks if object exists
					    	// streams return obj if found or else null
					    	Student std = studResult.stream()
					    			.filter(e -> e.getFullName().equalsIgnoreCase(request.get("name").trim()))
					    			.findFirst()
					    			.orElse(null);
					    	
					    	// student name found in list
					    	if(std!=null) {
					    		System.out.println("Student already exists.");
							    response1.put("status","fail");
							    response1.put("reason","student name already exists");
							    response1.put("data",std.getFullName());
							    caper=true;
					    	}
					    	if (!caper) {
					    		// calling database method to insert new student into a database
					    		dbStatus = dataObj.insertNewStudent(request.get("name").trim(), Integer.parseInt(request.get("year").trim()));
					    		// if student grade is inserted successfully then send success status to client else send fail status
					    		if(dbStatus){
						    		response1.put("status","success");
						    		response1.put("data",request.get("name").trim());
						    	} else{
					    			response1.put("status","fail");
					    			response1.put("reason","something went wrong while performing db insert");
					    			response1.put("data",request.get("name"));
					    		}
					    	}
					   		// sending the response to client
					 		outObj.writeObject(response1);
					   		break;
				    	}
				   }
	        }
		}
		// Catch end of file exception
		catch (EOFException e){
			System.out.println("EOF:"+e.getMessage());
		} 
		// Catch input/output exception
		catch(IOException e) {
			System.out.println("readline:"+e.getMessage());
		} 
		// Catch class not found exception
		catch(ClassNotFoundException ex){
					 ex.printStackTrace();
		}
		finally{ 
			try {
			s.close();
			}
			catch (IOException e){
			/*close failed*/}
		}
	}
}
