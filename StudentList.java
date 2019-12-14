import java.io.Serializable;
import java.util.LinkedList;

public class StudentList implements Serializable
 {
 	private LinkedList<Student> student;
	
	public StudentList(LinkedList<Student> student) {
	     this.student = student;
	}

	public LinkedList<Student> getStudent() {
		return student;
	}

	public void setStudent(LinkedList<Student> student) {
		this.student = student;
	}

	@Override
	public String toString()
     {
     	String studentList = "";
        for (int i = 0; i < this.student.size(); i++)
            studentList += this.student.get(i).toString();
        return String.format("%s \n", studentList);
     }

  }

