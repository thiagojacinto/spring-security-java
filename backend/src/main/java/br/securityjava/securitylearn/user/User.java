package br.securityjava.securitylearn.user;

public class User {
	
	// Attributes
	
	private final Integer userId;
	private final String userName;
	
	// Constructor
	public User(Integer userId, String userName) {
		super();
		this.userId = userId;
		this.userName = userName;
	}

	// Getters:
	public Integer getUserId() {
		return userId;
	}


	public String getUserName() {
		return userName;
	}
	
	

}
