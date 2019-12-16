import java.io.Serializable;

public class Request implements Serializable
 {
	String type;
	String data;

	public Request(String type, String data) {
	     this.type = type;
	     this.data = data;
	}

	public String getType(){
		return type;
	}

	public String getData(){
		return data;
	}

	@Override
	public String toString(){
        return String.format("%s %s\n", type, data);
     }

  }

