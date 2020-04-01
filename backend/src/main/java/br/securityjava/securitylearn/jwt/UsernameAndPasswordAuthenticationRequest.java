package br.securityjava.securitylearn.jwt;

public class UsernameAndPasswordAuthenticationRequest {
	
	private String username;
	private String password;
	
	public UsernameAndPasswordAuthenticationRequest() {
		// empty constructor
	}
	
	// Getters and setters

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}	
