import java.io.Serializable;

public class GradedAssessment extends Assessment implements Serializable
 {
	private Grade grdObj;
	
	public GradedAssessment() {
	     grdObj = new Grade();
	}

  }

