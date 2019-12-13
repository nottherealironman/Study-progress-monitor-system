import java.io.Serializable;
import java.util.LinkedList;

public class Subject implements Serializable
 {
	private String name;
	private LinkedList<Assessment> assessment;
	
	public Subject(String name,LinkedList<Assessment> assessment) {
	     this.name = name;
	     this.assessment = assessment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<Assessment> getAssessment() {
		return assessment;
	}

	public void setAssessment(LinkedList<Assessment> assessment) {
		this.assessment = assessment;
	}

	@Override
	public String toString()
     {
     	String assessment = "";
        for (int i = 0; i < this.assessment.size(); i++)
            assessment += this.assessment.get(i).toString();
        return String.format("%s  %s \n", this.getName(), assessment);
     }
  }

