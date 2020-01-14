import java.io.Serializable;
import java.util.LinkedList;

public class Student implements Serializable
 {
 	// Declaration of member variables
 	private int studentID;
	private String fullName;
	private int yearLevel;
	private LinkedList<Subject> subject;
	
	// Constructor
	public Student(int studentID, String fullName, int yearLevel, LinkedList<Subject> subject) {
	     // Initilization of member variables
	     this.studentID = studentID;
	     this.fullName = fullName;
	     this.yearLevel = yearLevel;
	     this.subject = subject;
	}

	// Definition of Accessors methods
	public int getStudentID() {
		return studentID;
	}

	public String getFullName() {
		return fullName;
	}

	public int getYearLevel() {
		return yearLevel;
	}

	public LinkedList<Subject> getSubject() {
		return subject;
	}

	// Definition of mutators methods
	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setYearLevel(int yearLevel) {
		this.yearLevel = yearLevel;
	}

	public void setSubject(LinkedList<Subject> subject) {
		this.subject = subject;
	}

	// toString method definition
	@Override
	public String toString()
     {
     	String subject = "";
        for (int i = 0; i < this.subject.size(); i++)
            subject += this.subject.get(i).toString();
        return String.format("%d %s  %s  %s \n", this.getStudentID(), this.getFullName(), this.getYearLevel(), subject);
     }

  }

