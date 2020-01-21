/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


/**
 *
 * @author acer
 */
public class DatabaseUtility {
    private final  String MYSQL_URL  ;
    final  String DB_URL;
    private Connection sqlConnection,dbConnection;
    private Statement  statement;
    private final String dbCreateSQL;
    private final String USER_NAME;
    private final String PASSWORD;
    
    private final String USER_TBL_QRY;
    private final String STUDENT_TBL_QRY;
    private final String SUBJECT_TBL_QRY;
    private final String ASSESSMENT_TBL_QRY;
    private final String GRADE_TBL_QRY;
    private final String STUDENTGRADE_TBL_QRY;
    private final String INSERT_USER_QRY;
    private final String INSERT_STUDENT_QRY;
    private final String INSERT_SUBJECT_QRY;
    private final String INSERT_GRADE_QRY;
    private DatabaseMetaData dbmd;
    private final String DATABASE_NAME  = "spms";
    
    public DatabaseUtility(){
        
        MYSQL_URL = "jdbc:mysql://localhost:3306";
        DB_URL = MYSQL_URL +"/" + DATABASE_NAME;
        //initialise MySql usename and password 
        USER_NAME ="root";
        PASSWORD = "";
        statement = null;
        //sql query to create database.
        dbCreateSQL = "CREATE DATABASE " + DATABASE_NAME ;
        //sql queries to create User Table
        USER_TBL_QRY =  "CREATE TABLE `S_Admin` (" +
                "  `UserID` int(10) NOT NULL AUTO_INCREMENT," +
                "  `FullName` varchar(50) NOT NULL DEFAULT ''," +
                "  `Password` varchar(150) NULL," +
                "  PRIMARY KEY (`UserID`)" +
                ") ENGINE=MyISAM AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4;";

         //sql queries to create Student Table
        STUDENT_TBL_QRY =  "CREATE TABLE `S_Student` (" +
                            "  `StudentID` int(10) NOT NULL AUTO_INCREMENT," +
                            "  `FullName` varchar(50) NOT NULL DEFAULT ''," +
                            "  `Password` varchar(150) NULL," +
                            "  `YearLevel` int(10) NOT NULL," +
                            "  PRIMARY KEY (`StudentID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
         //sql queries to create Subject Table
        SUBJECT_TBL_QRY =  "CREATE TABLE `S_Subject` (" +
                            "  `SubjectID` int(10) NOT NULL AUTO_INCREMENT," +
                            "  `Name` varchar(255) NOT NULL DEFAULT ''," +
                            "  PRIMARY KEY (`SubjectID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
         //sql queries to create Assessment Table
        ASSESSMENT_TBL_QRY =  "CREATE TABLE `S_Assessment` (" +
                            "  `SubjectID` int(10) NOT NULL," +
                            "  `AssessmentID` varchar(5) NOT NULL," +
                            "  `Type` varchar(100) NOT NULL DEFAULT ''," +
                            "  `Topic` varchar(255) NOT NULL DEFAULT ''," +
                            "  `Format` varchar(100) NOT NULL DEFAULT ''," +
                            "  `DueDate` varchar(100) NOT NULL DEFAULT ''," +
                            "  PRIMARY KEY (`SubjectID`,`AssessmentID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
         //sql queries to create Grade Table
        GRADE_TBL_QRY =  "CREATE TABLE `S_Grade` (" +
                            "  `GradeId` int(10) NOT NULL AUTO_INCREMENT," +
                            "  `Achievement` varchar(255) NOT NULL," +
                            "  `Knowledge` varchar(255) NOT NULL," +
                            "  `Skill` varchar(255) NOT NULL," +
                            "  PRIMARY KEY (`GradeID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
        //sql queries to create StudentGrade Table
        STUDENTGRADE_TBL_QRY =  "CREATE TABLE `S_StudentGrade` (" +
                            "  `StudentID` int(10) NOT NULL," +
                            "  `SubjectID` int(10) NOT NULL," +
                            "  `AssessmentID` varchar(5) NOT NULL," +
                            "  `GradeID` int(10) NOT NULL," +
                            "  PRIMARY KEY (`StudentID`,`SubjectID`,`AssessmentID`,`GradeID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";

        // sql to INSERT User table
        INSERT_USER_QRY = "INSERT INTO `S_Admin` (`FullName`) VALUES " +
                            "('Rahul Dileep')," +
                            "('Wei Li'),"+
                            "('Steve Smith')";

        // sql to INSERT student table
        INSERT_STUDENT_QRY = "INSERT INTO `S_Student` (`StudentID`, `FullName`, `YearLevel`) VALUES ('1', 'John Clarke', '11')," +
                "('2', 'Peter White', '11')," +
                "('3', 'Lily Li', '11')," +
                "('4', 'Lisa Soon', '11')," +
                "('5', 'Tom Dixon', '11');";
        
        // sql to INSERT Subject table
        INSERT_SUBJECT_QRY = "INSERT INTO `S_Subject` (`Name`) VALUES "
                          + "('English')," +
                            "('Maths B')," +
                            "('Biology')," +
                            "('Business and Communication Technologies')," +
                            "('Religion and Ethics');";
        
        // sql to INSERT Grade table
        INSERT_GRADE_QRY = "INSERT INTO `S_Grade` (`Achievement`,`Knowledge`,`Skill`) VALUES "
                          + "('Very high', 'thorough understanding', 'uses a high level of skill in both familiar\n" +
"and new situations')," +
                            "('High', 'clear understanding', 'uses a high level of skill in familiar\n" +
"situations, and is beginning to use skills in\n" +
"new situations')," +
                            "('Sound', 'understanding', 'uses skills in situations familiar to them')," +
                            "('Developing', 'understands aspects of ', 'uses varying levels of skill in situations\n" +
"familiar to them')," +
                            "('Emerging', 'basic understanding', 'beginning to use skills in familiar\n" +
"situations');";
        
        
    }
    
    // Method to create database and tables
    public boolean createDBtables()
    {
        boolean dbExists = false;
        boolean tblUserExist = false;
        boolean tblStudentExist = false;
        boolean tblSubjectExist = false;
        boolean tblAssessmentExist = false;
        boolean tblGradeExist = false;
        boolean tblStudentGradeExist = false;
        //Register MySql database driver
        try {
	      Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MySQL JDBC Driver?");
            e.printStackTrace();
            return false;
        }
            System.out.println("MySQL JDBC Driver Registered!");
        //connect to MySql ;
        try {
                sqlConnection = DriverManager.getConnection(MYSQL_URL, USER_NAME, PASSWORD);
                statement = sqlConnection.createStatement();
        } catch (SQLException e) {
                  System.out.println("Connection Failed! Check output console");
                  e.printStackTrace();
                  return false;
        }
        //check whether the database exists.
         try {
             //get the list of databases
             ResultSet dbData = sqlConnection.getMetaData().getCatalogs();
             String databaseName ="";      
             //iterate each catalog in the ResultSet 
             while (dbData.next()) {
                        // Get the database name, which is at position 1
                      databaseName = dbData.getString(1);
                        // Test print of database names, can be removed
                        // System.out.printf("%s ",databaseName);
                      if (databaseName.equalsIgnoreCase(DATABASE_NAME) )
                         dbExists = true;
             }
             if (! dbExists)  //if database doesn't exist create database executing the query.
             {
                statement.executeUpdate(dbCreateSQL);
             }
             if (sqlConnection != null)
                sqlConnection.close();  //close the existing connection to connect to MySql
             //connect to Books database
             dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);                                 
             statement = dbConnection.createStatement();
             dbmd= dbConnection.getMetaData();
              // loop through the list of tables if the tables are already created
             ResultSet rs = dbmd.getTables(null, null, "%", null);
             
             while (rs.next()) {
                     if((rs.getString(3).equalsIgnoreCase("S_Admin")))
                         tblUserExist = true;
                     if((rs.getString(3).equalsIgnoreCase("S_Student")))
                        tblStudentExist = true;
                     if((rs.getString(3).equalsIgnoreCase("S_Subject")))
                         tblSubjectExist = true;
                     if((rs.getString(3).equalsIgnoreCase("S_Assessment")))
                         tblAssessmentExist = true;
                     if((rs.getString(3).equalsIgnoreCase("S_Grade")))
                         tblGradeExist = true;
                     if((rs.getString(3).equalsIgnoreCase("S_StudentGrade")))
                         tblStudentGradeExist = true;
             }
             //if any of the tables doesn't exist create table executing the query
             if (!tblUserExist){
                 statement.executeUpdate(USER_TBL_QRY);
                 statement.executeUpdate(INSERT_USER_QRY);
             }
             if (!tblStudentExist){
                statement.executeUpdate(STUDENT_TBL_QRY);
                statement.executeUpdate(INSERT_STUDENT_QRY);
            }
             if (!tblSubjectExist){
                 statement.executeUpdate(SUBJECT_TBL_QRY);
                 statement.executeUpdate(INSERT_SUBJECT_QRY);
             }
                
             if (!tblAssessmentExist){
                statement.executeUpdate(ASSESSMENT_TBL_QRY);
                this.addAssessmentRecord();
             }
             
             if (!tblGradeExist){
                statement.executeUpdate(GRADE_TBL_QRY);
                statement.executeUpdate(INSERT_GRADE_QRY);
             }
              if (!tblStudentGradeExist){
                statement.executeUpdate(STUDENTGRADE_TBL_QRY);
              }
        } catch (SQLException e) {
		        System.out.println("Connection Failed! Check output console");
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
                e.printStackTrace();
		        return false;
	    }
        return true;
    }
    
    
    // Method to read data from file and store to database
    public String[][] readDataFile(){
        Scanner input = null;
        String[][] res = new String[14][6];
        try{
            // Get file URL
            InputStream url = getClass().getResourceAsStream("COIT20257Ass2Data.csv");// refer to same location with class files
            input = new Scanner(url);
            int row = 0;
            String[] arr;
            // Read data from file and store in array
            while(input.hasNextLine()){
                arr = input.nextLine().split(",");
                if(row > 0){// skip the first line
                    res[row-1] = arr;
                }
                row++;
            }
            input.close();
        } 
        catch (NullPointerException e){
            System.err.println("Error opening or close the file. Please, include COIT20257Ass2Data.csv file in same folder as other class files"); 
            System.exit(0);
        }
        catch (NoSuchElementException elemntException) { // Read content
            System.err.println("File impropertly formed.");
        }
        catch (IllegalStateException stateException) { // Read content
            System.err.println("Error reading from file.");
        }
        return res;
    }
    
    // Method to add assessment record
    public boolean addAssessmentRecord(){
      // Declaring prepared statement
       PreparedStatement addRecord;
       PreparedStatement subjectQuery;
        try {
           if (dbConnection  == null)// connect to MySql 
              dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD); 
             String[][] result = this.readDataFile();
             // loop through data stored in file
             for(String[] res:result){
                // Creating prepared statement to insert record
                addRecord   = dbConnection.prepareStatement( "INSERT INTO S_Assessment " +
                        "(SubjectID, AssessmentID, Type, Topic,Format, DueDate)" +
                                   "VALUES (?,?,?,?,?,?)");  
                // DB query to fetch subject id from subject name
                subjectQuery   = dbConnection.prepareStatement( "SELECT * FROM S_Subject WHERE Name = ?");
                subjectQuery.setString(1, res[0]);
                ResultSet rs = subjectQuery.executeQuery();
                
                // add record to assessment table
                while (rs.next()) {
                    int subjectID = Integer.parseInt(rs.getString("SubjectID"));
                    addRecord.setInt(1, subjectID);
                }
                
                addRecord.setString(2, res[1]);
                addRecord.setString(3, res[2]);
                addRecord.setString(4, res[3]);
                addRecord.setString(5, res[4]);
                addRecord.setString(6, res[5]);
                addRecord.executeUpdate();
             }  
             
        } 
        catch(SQLException e) {
                 System.out.println("Connection Failed! Check output console");
                 System.out.println("SQLException: " + e.getMessage());
                 System.out.println("SQLState: " + e.getSQLState());
                 e.printStackTrace();
                 return false;
        }
        return true;
    }
    
    // Method to fetch assessment list from database
    public LinkedList<Assessment> fetchAssessmentList(int subjectID){
      // Declaring prepared statement
        PreparedStatement assessmentRec; 
        LinkedList<Assessment> result = new LinkedList<>();
        Assessment assessment;
        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            // get the list of assessment
            assessmentRec   = dbConnection.prepareStatement( "SELECT  * FROM S_Assessment WHERE SubjectID = ?;");
            assessmentRec.setInt(1, subjectID);
            ResultSet rs = assessmentRec.executeQuery();
            
            // Loop through assessment list and store in linked list of Assessment class
            while (rs.next()) {
                assessment = new Assessment(rs.getString("AssessmentID"), rs.getString("Type"), rs.getString("Topic"), rs.getString("Format"), rs.getString("DueDate"));
                result.add(assessment);
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
		e.printStackTrace();
	   }
        return result;
    }

    // Method to fetch subject list from database
    public LinkedList<Subject> fetchSubjectList(){
      // Declaring prepared statement
        PreparedStatement subjRec; 
        LinkedList<Subject> result = new LinkedList<>();
        LinkedList<Assessment> assessment;
        Subject subj;
        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            // get the list of subject
            subjRec   = dbConnection.prepareStatement( "SELECT * FROM S_Subject");
            ResultSet rs = subjRec.executeQuery();
            
             // Loop through subject list and store in linked list of Subject class
            while (rs.next()) {
                assessment = fetchAssessmentList(rs.getInt("SubjectID"));
                subj = new Subject(rs.getInt("SubjectID"), rs.getString("Name"), assessment);
                result.add(subj);
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
        e.printStackTrace();
       }
        return result;
    }

    // Method to fetch student list from database
    public LinkedList<Student> fetchStudentList(){
      // Declaring prepared statement
        PreparedStatement studRec; 
        LinkedList<Student> result = new LinkedList<>();
        LinkedList<Subject> subjects;
        Student stud;
        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            // get the list of Student
            studRec   = dbConnection.prepareStatement( "SELECT * FROM S_Student");
            ResultSet rs = studRec.executeQuery();
            
            // Loop through student record and store in linked list of Student class
            while (rs.next()) {
                subjects = fetchSubjectList();
                stud = new Student(rs.getInt("StudentID"),rs.getString("FullName"),rs.getInt("YearLevel"), subjects);
                result.add(stud);
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
        e.printStackTrace();
       }
        return result;
    }

    // Method to fetch grade list from database
    public LinkedList<Grade> fetchGradeList(){
      // Declaring prepared statement
        PreparedStatement gradeRec; 
        LinkedList<Grade> result = new LinkedList<>();
        Grade grade;
        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            // get the list of grade
            gradeRec   = dbConnection.prepareStatement( "SELECT * FROM S_Grade");
            ResultSet rs = gradeRec.executeQuery();
            
            // Loop through grade record and store in linked list of Grade class
            while (rs.next()) {
                grade = new Grade(rs.getString("Achievement"),rs.getString("Knowledge"), rs.getString("Skill"));
                result.add(grade);
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
        e.printStackTrace();
       }
        return result;
    }

    // Method to check if assessment is graded
     public boolean checkAssignmentGraded(int studentId, int subjectId, String assessmentId){
      // Declaring prepared statement
        PreparedStatement grdQuery;
        ResultSet rs;
        int subjectID;
        try{
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);
                 // get the list of student grade
                grdQuery   = dbConnection.prepareStatement( "SELECT * FROM S_Studentgrade WHERE StudentID = ? AND SubjectID = ? AND AssessmentID = ?");
                grdQuery.setInt(1, studentId);
                grdQuery.setInt(2, subjectId);
                grdQuery.setString(3, assessmentId);
                rs = grdQuery.executeQuery();
                
                // Check if rows is returned by query
                // if row exists then assessment is graded for assignment
                if(rs.isBeforeFirst()){
                  return true;
                }
        }
        catch(SQLException e) {
                 System.out.println("Connection Failed! Check output console");
                                    System.out.println("SQLException: " + e.getMessage());
                                    System.out.println("SQLState: " + e.getSQLState());
                 e.printStackTrace();
        }
        // Return false if assessment is not graded
        return false;
    }

    // Method to insert student grade
    public boolean insertStudentGrade(int studentId, int subjectId, String assessmentId, int gradeId){
      // Declaring prepared statement
       PreparedStatement addRecord;
       PreparedStatement updateRecord;
       ResultSet rs;
       boolean recordExist;
        try {
           if (dbConnection  == null)// connect to MySql 
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD); 
                // Call method to check if assignment is graded
                recordExist = checkAssignmentGraded(studentId, subjectId, assessmentId);
                // If assignment is already graded for student then just update previous record 
                if(recordExist){
                  updateRecord   = dbConnection.prepareStatement( "UPDATE S_Studentgrade " +
                        " SET GradeID = ? "+
                        " WHERE StudentID = ? AND SubjectID = ? AND AssessmentID = ? "); 
                  updateRecord.setInt(1, gradeId); 
                  updateRecord.setInt(2, studentId);
                  updateRecord.setInt(3, subjectId);
                  updateRecord.setString(4, assessmentId);
                  updateRecord.executeUpdate(); 
                }
                // If assignment is not graded for student then just insert new record 
                else{
                  addRecord   = dbConnection.prepareStatement( "REPLACE INTO S_Studentgrade " +
                        "(StudentID, SubjectID, AssessmentID, GradeID)" +
                                   "VALUES (?,?,?,?)");  
                  addRecord.setInt(1, studentId);
                  addRecord.setInt(2, subjectId);
                  addRecord.setString(3, assessmentId);
                  addRecord.setInt(4, gradeId);
                  addRecord.executeUpdate(); 
                }
             
        } 
        catch(SQLException e) {
                 System.out.println("Connection Failed! Check output console");
                 System.out.println("SQLException: " + e.getMessage());
                 System.out.println("SQLState: " + e.getSQLState());
                 e.printStackTrace();
                 return false;
        }
        return true;
    }

    // Method to fetch student grade from database
    public LinkedList<GradedAssessment> fetchStudentGrade(int studentId, int subjectId){
      // Declaring prepared statement
        PreparedStatement stdGradeQuery; 
        LinkedList<GradedAssessment> grdAssmnt = new LinkedList<>();
        ResultSet rs;

        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            
            // Query to fetch grade for assessment in particular subject for student 
            stdGradeQuery   = dbConnection.prepareStatement( "SELECT s.Name, a.*, sg.GradeID, g.* FROM S_Studentgrade as sg "+
                " INNER JOIN S_Subject AS s ON sg.SubjectID = s.SubjectID "+
                " INNER JOIN S_Assessment as a ON (sg.AssessmentID = a.AssessmentID AND sg.SubjectID = a.SubjectID) "+
                " INNER JOIN S_Grade as g ON g.GradeId = sg.GradeId "+
                " WHERE sg.StudentID = ? AND sg.SubjectID = ?;"); 
            stdGradeQuery.setInt(1, studentId);
            stdGradeQuery.setInt(2, subjectId);
            rs = stdGradeQuery.executeQuery();
            
            while (rs.next()) {
                Grade grade = new Grade(rs.getString("Achievement"),rs.getString("Knowledge"), rs.getString("Skill"));
                grdAssmnt.add(new GradedAssessment(rs.getString("AssessmentID"), rs.getString("Type"), rs.getString("Topic"), rs.getString("Format"), rs.getString("DueDate"), grade));
            }
            
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
        e.printStackTrace();
       }
       return grdAssmnt;
    }
    
    // Method to insert a new student
    public boolean insertNewStudent(String studentName, int yearLevel) {
    	// Declaring prepared statement
    	PreparedStatement addRecord;
    	try {
    		dbConnection = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
    		addRecord = dbConnection
    			.prepareStatement("INSERT INTO S_Student (FullName, YearLevel) VALUES (?,?);");
    		addRecord.setString(1, studentName);
    		addRecord.setInt(2, yearLevel);
    		addRecord.execute();
    	} catch (SQLException e) {
    		System.out.println("Connection Failed! Check output console");
    		System.out.println("SQLException: " + e.getMessage());
    		System.out.println("SQLState: " + e.getSQLState());
    		e.printStackTrace();
    		return false;
    	}
    return true;
    }

    /**
     *  Vertify student  > 0 student is existed == 0 student is not existed
     * @param fullName
     * @return
     */
    public int vertifyExsitingStu(String fullName){
        // Declaring prepared statement
        PreparedStatement Query;
        ResultSet rs;
        try {
            if (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);

            // Query
            Query = dbConnection.prepareStatement("SELECT * from S_Student WHERE FullName = ?;");
            Query.setString(1, fullName);
            rs = Query.executeQuery();
            if (rs.next()){
                if(rs.getString("Password")==null)
                    return 1;
                else
                    return 0;
            } else {
                return -1;
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *  Vertify admin  > 0 admin is existed == 0 admin is not existed
     * @param fullName
     * @return
     */
    public int vertifyExsitingAdmin(String fullName){
        // Declaring prepared statement
        PreparedStatement Query;
        ResultSet rs;
        try {
            if (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);

            // Query
            Query = dbConnection.prepareStatement("SELECT * from S_Admin WHERE FullName = ?;");
            Query.setString(1, fullName);
            rs = Query.executeQuery();
            if (rs.next()){
                if(rs.getString("Password")==null)
                    return 1;
                else
                    return 0;
            } else {
                return -1;
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *  Vertify student  > 0 student is existed == 0 student is not existed
     * @param userId
     * @return
     */
    public int vertifyExsitingStuById(int userId){
        // Declaring prepared statement
        PreparedStatement Query;
        ResultSet rs;

        try {
            if (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);

            // Query
            Query = dbConnection.prepareStatement("SELECT * from S_Student WHERE StudentID = ?;");
            Query.setInt(1, userId);
            rs = Query.executeQuery();
            if (rs.next()){
                if(rs.getString("Password")==null)
                    return 1;
                else
                    return 0;
            } else {
                return -1;
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *  Vertify admin  > 0 admin is existed == 0 admin is not existed
     * @param userId
     * @return
     */
    public int vertifyExsitingAdminById(int userId){
        // Declaring prepared statement
        PreparedStatement Query;
        ResultSet rs;

        try {
            if (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);

            // Query
            Query = dbConnection.prepareStatement("SELECT * from S_Admin WHERE UserID = ?;");
            Query.setInt(1, userId);
            rs = Query.executeQuery();
            if (rs.next()){
                if(rs.getString("Password")==null)
                    return 1;
                else
                    return 0;
            } else {
                return -1;
            }
        }catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     *
     * @param regInfo ['fullName', ''userType', 'password']
     * @return
     */
    public int userRegister(HashMap<String, String> regInfo){
        // Declaring prepared statement
        PreparedStatement addRecord;
        PreparedStatement searchQuery;
        ResultSet rs;
        String tableName;
        int userId = 0;
        try {
            if (dbConnection  == null)// connect to MySql
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);

            if(regInfo.get("userType").equals("1"))
                tableName = "S_Admin";
            else
                tableName = "S_Student";
            // update password
            addRecord   = dbConnection.prepareStatement( "UPDATE " +  tableName  +
                    " SET Password = ? " +
                    " WHERE FullName = ?");
            addRecord.setString(1, regInfo.get("password"));
            addRecord.setString(2, regInfo.get("fullName"));
            addRecord.executeUpdate();

            // fetch user by fullname
            searchQuery   = dbConnection.prepareStatement( "SELECT * FROM " + tableName + " WHERE FullName = ?");
            searchQuery.setString(1, regInfo.get("fullName"));
            rs = searchQuery.executeQuery();

            while (rs.next()) {
                if(regInfo.get("userType").equals("1"))
                    userId = Integer.parseInt(rs.getString("UserID"));
                else
                    userId = Integer.parseInt(rs.getString("StudentID"));
            }
        }
        catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
        return userId;
    }

    /**
     *
     * @param LogInfo ['userId', 'userType', 'password']
     * @return
     */
    public String userLogin(HashMap<String, String> logInfo){
        // Declaring prepared statement
        PreparedStatement addRecord;
        PreparedStatement searchQuery;
        ResultSet rs;
        String tableName;
        String colName;
        String userName = null;
        try {
            if (dbConnection  == null)// connect to MySql
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);

            if(logInfo.get("userType").equals("1")){
                tableName = "S_Admin";
                colName = "UserID";
            }
            else{
                tableName = "S_Student";
                colName = "StudentID";
            }

            // Fetch user by userid and password
            searchQuery   = dbConnection.prepareStatement( "SELECT * FROM " + tableName + " WHERE "+colName+" = ? AND Password = ?");
            searchQuery.setInt(1, Integer.parseInt(logInfo.get("UserId")));
            searchQuery.setString(2, logInfo.get("password"));
            rs = searchQuery.executeQuery();

            // if row exists then user password is correct
            //if(rs.isBeforeFirst()){

            if(rs.next()){
                userName = rs.getString("FullName");
            }
        }
        catch(SQLException e) {
            System.out.println("Connection Failed! Check output console");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            e.printStackTrace();
        }
        return userName;
    }

}
