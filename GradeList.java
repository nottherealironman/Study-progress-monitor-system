import java.io.Serializable;
import java.util.LinkedList;

public class GradeList implements Serializable
 {
 	private LinkedList<Grade> grade;
	
	public GradeList(LinkedList<Grade> grade) {
	     this.grade = grade;
	}

	public LinkedList<Grade> getGrade() {
		return grade;
	}

	public void setGrade(LinkedList<Grade> grade) {
		this.grade = grade;
	}

	@Override
	public String toString()
     {
     	String gradeList = "";
        for (int i = 0; i < this.grade.size(); i++)
            gradeList += this.grade.get(i).toString();
        return String.format("%s \n", gradeList);
     }

  }

