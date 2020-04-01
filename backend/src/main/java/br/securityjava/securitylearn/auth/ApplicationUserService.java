package br.securityjava.securitylearn.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {
	/*
	 * The use of an Interface allows flexibility of implementation with 
	 * real DAOs, no matter the customized logic.
	 */
	private final ApplicationUserDAO applicationUserDAO;
	
	@Autowired	// Qualifier tells which implemention to use
	public ApplicationUserService(@Qualifier("fake") ApplicationUserDAO applicationUserDAO) {
		this.applicationUserDAO = applicationUserDAO;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		return applicationUserDAO
				.selectAppUserByUsername(username)
				.orElseThrow(
						() -> new UsernameNotFoundException(String.format("Username %s not found", username))
						);
	}

}
