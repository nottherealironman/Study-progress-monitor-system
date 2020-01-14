import java.io.Serializable;

public class Grade implements Serializable
 {
 	// Declaration of member variables
	private String achievement;
	private String knowledge;
	private String skill;
	
	// Constructor
	public Grade(String achievement, String knowledge, String skill) {
		// Initilization of member variables
	     this.achievement = achievement;
	     this.knowledge = knowledge;
	     this.skill = skill;
	}

	// Definition of Accessors methods
	public String getAchievement() {
		return achievement;
	}

	public String getKnowledge() {
		return knowledge;
	}

	public String getSkill() {
		return skill;
	}

	// Definition of mutators methods
	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}
	
	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}
	
	public void setSkill(String skill) {
		this.skill = skill;
	}

	// toString method definition
	@Override
    public String toString(){
        return String.format(" Achievement:%s\n Knowledge:%s\n Skill:%s \n", 
                this.achievement, this.knowledge, this.skill);
    }

  }

