import java.io.Serializable;
import java.util.LinkedList;

public class Subject implements Serializable
 {
	private String name;
	private LinkedList<Assessment> assessment;
	
	public Subject() {
	     
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

  }

