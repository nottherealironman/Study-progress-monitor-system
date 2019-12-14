import java.io.Serializable;
import java.util.LinkedList;

public class Student implements Serializable
 {
	private String fullName;
	private int yearLevel;
	private LinkedList<Subject> subject;
	
	public Student(String fullName, int yearLevel, LinkedList<Subject> subject) {
	     this.fullName = fullName;
	     this.yearLevel = yearLevel;
	     this.subject = subject;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getYearLevel() {
		return yearLevel;
	}

	public void setYearLevel(int yearLevel) {
		this.yearLevel = yearLevel;
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
     	String subject = "";
        for (int i = 0; i < this.subject.size(); i++)
            subject += this.subject.get(i).toString();
        return String.format("%s  %s  %s \n", this.getFullName(), this.getYearLevel(), subject);
     }

  }

