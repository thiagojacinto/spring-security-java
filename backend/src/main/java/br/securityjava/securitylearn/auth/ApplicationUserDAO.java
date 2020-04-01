package br.securityjava.securitylearn.auth;

import java.util.Optional;

public interface ApplicationUserDAO {
	
	/*
	 * 	Being an interface facilitates the change of the real implementation
	 */
	
	Optional<ApplicationUser> selectAppUserByUsername(String username);
	
}
