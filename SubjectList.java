import java.io.Serializable;
import java.util.LinkedList;

public class SubjectList implements Serializable
 {
 	private LinkedList<Subject> subject;
	
	public SubjectList(LinkedList<Subject> subject) {
	     this.subject = subject;
	}

	public LinkedList<Subject> getSubject() {
		return subject;
	}

	public void setSubject(LinkedList<Subject> subject) {
		this.subject = subject;
	}

	@Override
	public String toString()
     {
     	String subjectList = "";
        for (int i = 0; i < this.subject.size(); i++)
            subjectList += this.subject.get(i).toString();
        return String.format("%s \n", subjectList);
     }

  }

