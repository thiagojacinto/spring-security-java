package br.securityjava.securitylearn.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import br.securityjava.securitylearn.security.ApplicationUserRole;

@Repository("fake")	// Tells Spring to instantiate it, and use that name to autowire
public class FakeApplicationUserDAOService implements ApplicationUserDAO {
	
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public FakeApplicationUserDAOService(PasswordEncoder passwordEncoder) {
		
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public Optional<ApplicationUser> selectAppUserByUsername(String username) {
		
		return getApplicationUsers()
				.stream()
				.filter(appUser -> username.equals(appUser.getUsername()))
				.findFirst();
	}
	
	private List<ApplicationUser> getApplicationUsers() {
		
		List<ApplicationUser> applicationUsers = Lists.newArrayList(
				
				new ApplicationUser(
						passwordEncoder.encode("password"), 
						"thiagojacinto",
						ApplicationUserRole.USER.getGrantedAuthorities(), 
						true, true, true, true
						),
			
				new ApplicationUser(
						passwordEncoder.encode("password"), 
						"linda",
						ApplicationUserRole.ADMIN.getGrantedAuthorities(), 
						true, true, true, true
						),
				
				new ApplicationUser(
						passwordEncoder.encode("password"), 
						"tomhashtag",
						ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities(), 
						true, true, true, true
						)
				);
		
		return applicationUsers;
	}

}
