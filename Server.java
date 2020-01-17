import java.net.*;
import java.io.*;
import java.util.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;

public class Server {
	private static int clientCount;
	private final KeyPairGenerator keyPairGen;
	private final PrivateKey privateKey;
	private final PublicKey publicKey;

	public Server() throws NoSuchAlgorithmException {
		keyPairGen =  KeyPairGenerator.getInstance("RSA");
		KeyPair keyPair = keyPairGen.genKeyPair();
		this.privateKey = keyPair.getPrivate();
		this.publicKey = keyPair.getPublic();

	}

	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}
	public PublicKey getPublicKey() {
		return publicKey;
	}
	public KeyPairGenerator getKeyPairGen() {
		return keyPairGen;
	}
	public String decrypt(byte [] encodedMessage) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey, cipher.getParameters());
		return new String(cipher.doFinal(encodedMessage));
	}

	public static void main (String args[]) {
		
		try{
			// Initilizing server port
			int serverPort = 8888; 
			// Creating socket to communicate with client
			ServerSocket listenSocket = new ServerSocket(serverPort);
			int i=0;
			Server tcpServer = new Server();
			PublicKey publicKey =  tcpServer.getPublicKey();
			System.out.println("Server started");
			while(true) {
				// Creating socket to listen to client request
				Socket clientSocket = listenSocket.accept();
				clientCount++;
				// Connection class to create threads to handle multiple clients
				Connection c = new Connection(clientSocket, i++,clientCount, publicKey, tcpServer.getPrivateKey());
			}

		} catch(IOException e) {System.out.println("Listen socket:"+e.getMessage());}
		catch (NoSuchAlgorithmException e){ System.out.println("Algorithm: "+ e.getMessage()); }
	}
}
class Connection extends Thread {
	
	// Declaration of classes, variables, list and streams
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
	LinkedList<GradedAssessment> stdGrd;
	LinkedList<String> menuList;
	SubjectList subjList;
	StudentList studList;
	GradeList grdList;
	GradedAssessment grdAssmnt;
	HashMap<String, String> request;
	String orgRequest;
	boolean dbStatus;
	String requestType;
	String printMsg;
	PublicKey publicKey;
	PrivateKey privateKey;
	HashMap<String, String> RegInfo = new HashMap<String, String>();
	HashMap<String, String> LogInfo = new HashMap<String, String>();
	HashMap<String, String> response;
	public Connection (Socket aClientSocket, int tn, int client,PublicKey key, PrivateKey privateKey) {
		publicKey =key;
		this.privateKey = privateKey;
		try {
			thrdn=tn;
			clientCount =client;
			s = aClientSocket;
			
			// Initializing Input and Output object stream to communicate with clients
			inObj = new ObjectInputStream(s.getInputStream());
	        outObj =new ObjectOutputStream(s.getOutputStream());
	        inData = new DataInputStream(s.getInputStream());
	        outData =new DataOutputStream(s.getOutputStream());

	      	System.out.println("Client count: "+client);
	      	// Calling start method of thread to handle client request
			this.start();
		} catch(IOException e) {System.out.println("Connection:"+e.getMessage());}
	}

	public String decrypt(byte [] encodedMessage) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey, cipher.getParameters());
		return new String(cipher.doFinal(encodedMessage));
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
					case "hello":
						// Register
						if(request.get("LoginType") != null && request.get("LoginType").equals("1")){
							if(request.get("UserName") != null && RegInfo.get("UserName") == null) {
								int statusId;
								if(RegInfo.get("userType").equals("2")) {
									// vertify unique student
									statusId = dataObj.vertifyExsitingStu(request.get("UserName"));

								} else {
									statusId = dataObj.vertifyExsitingAdmin(request.get("UserName"));
								}
								// -1 user not existed;
								if(statusId == -1) {
									outData.writeUTF("This user is not existed");
									outData.writeInt(0);
									break;
								} else if(statusId == 0) {
									outData.writeUTF("This user already registered");
									outData.writeInt(0);
									break;
								}

								RegInfo.put("fullName",request.get("UserName"));
								outData.writeUTF("Please type your password");
								//generate the encoded key
								byte[] bytesPubKey = publicKey.getEncoded();
								System.out.println("PublicKey size in bytes: " +bytesPubKey.length);
								//send the keysize;
								outData.writeInt(bytesPubKey.length);
								//send the key in bytes
								outData.write(bytesPubKey, 0, bytesPubKey.length);
								break;
							} else if(request.get("userType") != null){
								RegInfo.put("userType",request.get("userType"));
								outData.writeUTF("Please type your name:");
								break;
							}
							outData.writeUTF("Please select user type:\n 1 Administrator\n 2 Studnet");
							break;
						}
						// Login
						else if (request.get("LoginType") != null && request.get("LoginType").equals("2")){
							if(request.get("UserId") != null) {
								// verify if user is registered or not
								int statusId;
								if(LogInfo.get("userType").equals("2")) {
									// vertify unique student
									statusId = dataObj.vertifyExsitingStuById(Integer.parseInt(request.get("UserId")));

								} else {
									statusId = dataObj.vertifyExsitingAdminById(Integer.parseInt(request.get("UserId")));
								}

								// -1 user not exist;
								if(statusId == -1) {
									outData.writeUTF("Sorry, the entered UserId does not exist");
									outData.writeInt(0);
									break;
								} else if(statusId == 1) {
									outData.writeUTF("Sorry, the User is not registered");
									outData.writeInt(0);
									break;
								}

								LogInfo.put("UserId",request.get("UserId"));
								outData.writeUTF("Please type your password");
								//generate the encoded key
								byte[] bytesPubKey = publicKey.getEncoded();
								System.out.println("PublicKey size in bytes: " +bytesPubKey.length);
								//send the keysize;
								outData.writeInt(bytesPubKey.length);
								//send the key in bytes
								outData.write(bytesPubKey, 0, bytesPubKey.length);
								break;
							}
							else if(request.get("userType") != null){
								LogInfo.put("userType",request.get("userType"));
								outData.writeUTF("Please, Enter your User Id:");
								break;
							}
							outData.writeUTF("Please select user type:\n 1 Administrator\n 2 Student");
							break;
						}
						printMsg = "1 New User\n2 Registered User";
						outData.writeUTF(printMsg);
						break;
					case "register":
						int msgLength = inData.readInt();
						//read the size of encrypted message to be sent from client
						byte [] encodedmessage = new byte [msgLength];
						//read the encryped password sent from client
						inData.read(encodedmessage,0, encodedmessage.length);
						RegInfo.put("password",decrypt(encodedmessage));
						int userId = dataObj.userRegister(RegInfo);
						if(userId > 0){
							outData.writeUTF("Registration successed\n Your ID is: " + userId);
						} else {
							outData.writeUTF("Registration failed");
						}
						break;

					case "login":
						int logMsgLength = inData.readInt();
						//read the size of encrypted message to be sent from client
						byte [] logEncodedmessage = new byte [logMsgLength];
						//read the encryped password sent from client
						inData.read(logEncodedmessage,0, logEncodedmessage.length);
						LogInfo.put("password",decrypt(logEncodedmessage));
						boolean status = dataObj.userLogin(LogInfo);
						if(status){
							outData.writeUTF("Login successfull");
						} else {
							outData.writeUTF("Login failed");
						}
						break;

					case "view-assessment-request":
						// calling database method to fetch subjects
						subjResult = dataObj.fetchSubjectList();
						// Storing list of subjects in SubjectList
	        			subjList = new SubjectList(subjResult);
	        			// sending the response to client
				        outObj.writeObject(subjList);
				        break;

				    case "student-list-request":
				    	// calling database method to fetch students
				    	studResult = dataObj.fetchStudentList();
				    	// Storing list of students in StudentList
						studList = new StudentList(studResult);
						// sending the response to client
				    	outObj.writeObject(studList);
				    	break;

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
		catch (NoSuchAlgorithmException e){
			System.out.println("Algorithm: "+ e.getMessage());
		}
		catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e){
			System.out.println("invalid key spec: "+ e.getMessage());
		}
		catch (InvalidKeyException e){
			System.out.println("invalid key: "+ e.getMessage());
		} catch (InvalidAlgorithmParameterException ex) {
			Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
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
