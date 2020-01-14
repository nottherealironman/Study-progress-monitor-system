import java.io.Serializable;

public class Assessment implements Serializable
 {

 	
 	// Declaration of member variables
	private String assessmentID;
	private String type;
	private String topic;
	private String format;
	private String dueDate;
	
	// Constructor
	public Assessment(String assessmentID, String type, String topic, String format, String dueDate) {
	     // Initilization of member variables
	     this.assessmentID = assessmentID;
	     this.type = type;
	     this.topic = topic;
	     this.format = format;
	     this.dueDate = dueDate;
	}

	// Definition of Accessors methods
	public String getAssessmentID() {
		return assessmentID;
	}

	public String getType() {
		return type;
	}

	public String getTopic() {
		return topic;
	}

	public String getFormat() {
		return type;
	}

	public String getDueDate() {
		return dueDate;
	}

	// Definition of mutators methods
	public void setAssessmentID(String assessmentID) {
		this.assessmentID = assessmentID;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}
	
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	// toString method definition
	@Override
    public String toString(){
        return String.format(" AssessmentID:%s\n Type:%s\n Topic:%s\n Format:%s\n Due date:%s \n", 
                this.assessmentID, this.type, this.topic, this.format, this.dueDate);
    }

  }

