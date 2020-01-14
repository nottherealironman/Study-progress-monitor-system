import java.io.Serializable;
import java.util.LinkedList;

public class SubjectList implements Serializable
 {
 	// Declaration of member variables
 	private LinkedList<Subject> subject;
	
	// Constructor
	public SubjectList(LinkedList<Subject> subject) {
	     this.subject = subject;
	}

	// Definition of Accessors methods
	public LinkedList<Subject> getSubject() {
		return subject;
	}

	// Definition of mutators methods
	public void setSubject(LinkedList<Subject> subject) {
		this.subject = subject;
	}

	// toString method definition
	@Override
	public String toString()
     {
     	String subjectList = "";
        for (int i = 0; i < this.subject.size(); i++)
            subjectList += this.subject.get(i).toString();
        return String.format("%s \n", subjectList);
     }

  }

