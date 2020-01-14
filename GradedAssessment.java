import java.io.Serializable;

public class GradedAssessment extends Assessment implements Serializable
 {  
    // Creating object of Grade
	private Grade grd;
	
	public GradedAssessment(String assessmentID, String type, String topic, String format, String dueDate, Grade grd) {
	     // passing arguments to parent class
         super(assessmentID, type, topic, format, dueDate);
	     this.grd = grd;
	}

    // Definition of Accessors methods
	public Grade getGrade(){
        return this.grd;
    }

    // Definition of mutators methods
	public void setGrade(Grade grd){
        this.grd = grd;
    }

    // toString method definition
    @Override
    public String toString(){
        return String.format(" %s\n %s\n", 
                super.toString(),this.grd);
    }
   
  }

