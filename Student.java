import java.io.Serializable;
import java.util.LinkedList;

public class Student implements Serializable
 {
	private String fullName;
	private int yearLevel;
	private LinkedList<Subject> subject;
	
	public Student() {
	     
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

	public void setYearLevel(int yearLevels) {
		this.yearLevel = yearLevel;
	}

  }

