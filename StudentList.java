import java.io.Serializable;
import java.util.LinkedList;

public class StudentList implements Serializable
 {
 	// Declaration of member variables
 	private LinkedList<Student> student;
	
	// Constructor
	public StudentList(LinkedList<Student> student) {
	     this.student = student;
	}

	// Definition of Accessors methods
	public LinkedList<Student> getStudent() {
		return student;
	}

	// Definition of mutators methods
	public void setStudent(LinkedList<Student> student) {
		this.student = student;
	}

	// toString method definition
	@Override
	public String toString()
     {
     	String studentList = "";
        for (int i = 0; i < this.student.size(); i++)
            studentList += this.student.get(i).toString();
        return String.format("%s \n", studentList);
     }

  }

