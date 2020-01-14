import java.io.Serializable;
import java.util.LinkedList;

public class Subject implements Serializable
 {
 	// Declaration of member variables
 	private int subjectId;
	private String name;
	private LinkedList<Assessment> assessment;
	
	// Constructor
	public Subject(int subjectId, String name,LinkedList<Assessment> assessment) {
		 // Initilization of member variables
		 this.subjectId = subjectId;
	     this.name = name;
	     this.assessment = assessment;
	}

	// Definition of Accessors methods
	public int getId() {
		return subjectId;
	}

	public String getName() {
		return name;
	}
	
	public LinkedList<Assessment> getAssessment() {
		return assessment;
	}

	// Definition of mutators methods
	public void setId(int subjectId) {
		this.subjectId = subjectId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setAssessment(LinkedList<Assessment> assessment) {
		this.assessment = assessment;
	}

	// toString method definition
	@Override
	public String toString()
     {
     	String assessment = "";
        for (int i = 0; i < this.assessment.size(); i++)
            assessment += this.assessment.get(i).toString();
        return String.format("%d %s  %s \n", this.getId(), this.getName(), assessment);
     }
  }

