import java.io.Serializable;

public class Grade implements Serializable
 {
	private String achievement;
	private String knowledge;
	private String skill;
	
	public Grade(String achievement, String knowledge, String skill) {
	     this.achievement = achievement;
	     this.knowledge = knowledge;
	     this.skill = skill;
	}

	public String getAchievement() {
		return achievement;
	}

	public void setAchievement(String achievement) {
		this.achievement = achievement;
	}

	public String getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(String knowledge) {
		this.knowledge = knowledge;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

  }

