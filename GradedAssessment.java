import java.io.Serializable;

public class GradedAssessment extends Assessment implements Serializable
 {
	private Grade grd;
	
	public GradedAssessment(String assessmentID, String type, String topic, String format, String dueDate, Grade grd) {
	     super(assessmentID, type, topic, format, dueDate);
	     this.grd = grd;
	}

	public Grade getGrade(){
        return this.grd;
    }

	public void setGrade(Grade grd){
        this.grd = grd;
    }
   
  }

