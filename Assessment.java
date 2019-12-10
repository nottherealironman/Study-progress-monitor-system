import java.io.Serializable;
import java.util.LinkedList;

public class Assessment implements Serializable
 {
	private String assessmentID;
	private String type;
	private String topic;
	private String format;
	private String dueDate;
	
	public Assessment() {
	     
	}

	public String getAssessmentID() {
		return assessmentID;
	}

	public void setAssessmentID(String assessmentID) {
		this.assessmentID = assessmentID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getFormat() {
		return type;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

  }

