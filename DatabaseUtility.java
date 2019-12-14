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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
    
    private final String STUDENT_TBL_QRY;
    private final String SUBJECT_TBL_QRY;
    private final String ASSESSMENT_TBL_QRY;
    private final String GRADE_TBL_QRY;
    private final String STUDENTGRADE_TBL_QRY;
    private final String INSERT_STUDENT_QRY;
    private final String INSERT_SUBJECT_QRY;
    private final String INSERT_GRADE_QRY;
    private DatabaseMetaData dbmd;
    private final String DATABASE_NAME  = "SPMS";
    
    public DatabaseUtility(){
        
        MYSQL_URL = "jdbc:mysql://localhost:3306";
        DB_URL = MYSQL_URL +"/" + DATABASE_NAME;
        //initialise MySql usename and password 
        USER_NAME ="root";
        PASSWORD = "";
        statement = null;
        //sql query to create database.
        dbCreateSQL = "CREATE DATABASE " + DATABASE_NAME ;
         //sql queries to create Student Table
        STUDENT_TBL_QRY =  "CREATE TABLE `Student` (" +
                            "  `StudentID` int(10) NOT NULL AUTO_INCREMENT," +
                            "  `FullName` varchar(50) NOT NULL DEFAULT ''," +
                            "  `YearLevel` int(10) NOT NULL," +
                            "  PRIMARY KEY (`StudentID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
         //sql queries to create Subject Table
        SUBJECT_TBL_QRY =  "CREATE TABLE `Subject` (" +
                            "  `SubjectID` int(10) NOT NULL AUTO_INCREMENT," +
                            "  `Name` varchar(255) NOT NULL DEFAULT ''," +
                            "  PRIMARY KEY (`SubjectID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
         //sql queries to create Assessment Table
        ASSESSMENT_TBL_QRY =  "CREATE TABLE `Assessment` (" +
                            "  `SubjectID` int(10) NOT NULL," +
                            "  `AssessmentID` varchar(5) NOT NULL," +
                            "  `Type` varchar(100) NOT NULL DEFAULT ''," +
                            "  `Topic` varchar(255) NOT NULL DEFAULT ''," +
                            "  `Format` varchar(100) NOT NULL DEFAULT ''," +
                            "  `DueDate` varchar(100) NOT NULL DEFAULT ''," +
                            "  PRIMARY KEY (`SubjectID`,`AssessmentID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
         //sql queries to create Grade Table
        GRADE_TBL_QRY =  "CREATE TABLE `Grade` (" +
                            "  `GradeId` int(10) NOT NULL AUTO_INCREMENT," +
                            "  `Achievement` varchar(255) NOT NULL," +
                            "  `Knowledge` varchar(255) NOT NULL," +
                            "  `Skill` varchar(255) NOT NULL," +
                            "  PRIMARY KEY (`GradeID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
        //sql queries to create StudentGrade Table
        STUDENTGRADE_TBL_QRY =  "CREATE TABLE `StudentGrade` (" +
                            "  `StudentID` int(10) NOT NULL," +
                            "  `SubjectID` int(10) NOT NULL," +
                            "  `AssessmentID` int(10) NOT NULL," +
                            "  `GradeID` int(10) NOT NULL," +
                            "  PRIMARY KEY (`StudentID`,`SubjectID`,`AssessmentID`,`GradeID`)" +
                            ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb4;";
        
        // sql to INSERT student table
        INSERT_STUDENT_QRY = "INSERT INTO `Student` VALUES "
                          + "('1210789', 'John Clarke', '11')," +
                            "('1234564', 'Peter White', '11')," +
                            "('1234378', 'Lily Li', '11')," +
                            "('2371231', 'Lisa Soon', '11')," +
                            "('1283742', 'Tom Dixon', '11');";
        
        // sql to INSERT Subject table
        INSERT_SUBJECT_QRY = "INSERT INTO `Subject` (`Name`) VALUES "
                          + "('English')," +
                            "('Maths B')," +
                            "('Biology')," +
                            "('Business and Communication Technologies')," +
                            "('Religion and Ethics');";
        
        // sql to INSERT Grade table
        INSERT_GRADE_QRY = "INSERT INTO `Grade` (`Achievement`,`Knowledge`,`Skill`) VALUES "
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
    
    public boolean createDBtables()
    {
        boolean dbExists = false;
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
        //chack whether the databse exists.
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
                     if((rs.getString(3).equalsIgnoreCase("Student")))
                       tblStudentExist = true;
                     if((rs.getString(3).equalsIgnoreCase("Subject")))
                         tblSubjectExist = true;
                     if((rs.getString(3).equalsIgnoreCase("Assessment")))
                         tblAssessmentExist = true;
                     if((rs.getString(3).equalsIgnoreCase("Grade")))
                         tblGradeExist = true;
                     if((rs.getString(3).equalsIgnoreCase("StudentGrade")))
                         tblStudentGradeExist = true;
             }
             //if any of the tables doesn't exist create table executing the query
          
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
    
    /**
     * login
     * @return 
     */
    
    
    public String[][] readDataFile(){
        Scanner input = null;
        String[][] res = new String[14][6];
        try{
            InputStream url = getClass().getResourceAsStream("COIT20257Ass2Data.csv");// refer to same location with class files
            input = new Scanner(url);
            int row = 0;
            String[] arr;
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
            System.err.println("Error opening or close the file."); 
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
    
    public boolean addAssessmentRecord(){
       PreparedStatement addRecord;
       PreparedStatement subjectQuery;
        try {
           if (dbConnection  == null)// connect to MySql 
              dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD); 
             String[][] result = this.readDataFile();
             for(String[] res:result){
                //System.out.println(res[0]);
                addRecord   = dbConnection.prepareStatement( "INSERT INTO Assessment " +
                        "(SubjectID, AssessmentID, Type, Topic,Format, DueDate)" +
                                   "VALUES (?,?,?,?,?,?)");  
                subjectQuery   = dbConnection.prepareStatement( "SELECT * FROM subject WHERE Name = ?"); 
                subjectQuery.setString(1, res[0]);
                ResultSet rs = subjectQuery.executeQuery();
                
                while (rs.next()) {
                    System.out.println(rs.getString("SubjectID"));
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
    
    public LinkedList<Assessment> fetchAssessmentList(int subjectID){
        PreparedStatement assessmentRec; 
        LinkedList<Assessment> result = new LinkedList<>();
        Assessment assessment;
        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            // get the list of Energy_needs
            assessmentRec   = dbConnection.prepareStatement( "SELECT  * FROM assessment WHERE SubjectID = ?;"); 
            assessmentRec.setInt(1, subjectID);
            ResultSet rs = assessmentRec.executeQuery();
            
            while (rs.next()) {
                /*result.add(rs.getString("AssessmentID"));
                result.add(rs.getString("Type"));
                result.add(rs.getString("Topic"));
                result.add(rs.getString("Format"));
                result.add(rs.getString("DueDate"));*/
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

    public LinkedList<Subject> fetchSubjectList(){
        PreparedStatement subjRec; 
        LinkedList<Subject> result = new LinkedList<>();
        LinkedList<Assessment> assessment;
        Subject subj;
        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            // get the list of Energy_needs
            subjRec   = dbConnection.prepareStatement( "SELECT * FROM subject"); 
            ResultSet rs = subjRec.executeQuery();
            
            while (rs.next()) {
                //result.add(rs.getString("Name"));
                assessment = fetchAssessmentList(rs.getInt("SubjectID"));
                subj = new Subject(rs.getString("Name"), assessment);
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

    public LinkedList<Student> fetchStudentList(){
        PreparedStatement studRec; 
        LinkedList<Student> result = new LinkedList<>();
        LinkedList<Subject> subjects;
        Student stud;
        try {
            if  (dbConnection  == null)//connect to MySql ;
                dbConnection = DriverManager.getConnection (DB_URL, USER_NAME, PASSWORD);   
            // get the list of Student
            studRec   = dbConnection.prepareStatement( "SELECT * FROM student"); 
            ResultSet rs = studRec.executeQuery();
            
            while (rs.next()) {
                //result.add(rs.getString("Name"));
                subjects = fetchSubjectList();
                stud = new Student(rs.getString("FullName"),rs.getInt("YearLevel"), subjects);
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
}
