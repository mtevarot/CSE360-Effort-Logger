package application;

public class Login {
	private String name; 
	private String password; 
	
	public void setName(String name) {
		this.name = name; 
	}
	
	public void setPassword(String password) {
		this.password = password; 
	}
	
	public String getName() {
		return this.name; 
	}
	
	public String getPassword() {
		return this.password; 
	}
	
	public boolean validateLogin(String u, String p) {
		if("d".equals(u) && "d".equals(p))
		{
			return true;
		}
		return false; 
	}
}
