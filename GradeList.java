import java.io.Serializable;
import java.util.LinkedList;

public class GradeList implements Serializable
 {
 	private LinkedList<Grade> grade;
	
	// Constructor
	public GradeList(LinkedList<Grade> grade) {
	     this.grade = grade;
	}

	// Definition of Accessors methods
	public LinkedList<Grade> getGrade() {
		return grade;
	}

	// Definition of mutators methods
	public void setGrade(LinkedList<Grade> grade) {
		this.grade = grade;
	}

	// toString method definition
	@Override
	public String toString()
     {
     	String gradeList = "";
        for (int i = 0; i < this.grade.size(); i++)
            gradeList += this.grade.get(i).toString();
        return String.format("%s \n", gradeList);
     }

  }

