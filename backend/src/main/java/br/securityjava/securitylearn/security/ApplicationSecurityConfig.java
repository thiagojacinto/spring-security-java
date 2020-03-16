package br.securityjava.securitylearn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	// Attributes
	private final PasswordEncoder passwordEncoder;	// calls encoder to deal with password
	
	// Constructor
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {

		this.passwordEncoder = passwordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		 * ENFORCE authenticity of a client, for every request
		 */
		
		http
			.authorizeRequests()
			.antMatchers("/", "index", "/css/index.css", "/js/index.js")	// Used to whitelist access permission
			.permitAll()	// then, permits access to predefined Matchers
			.anyRequest()
			.authenticated()
			.and()
			.httpBasic();
		
	}
	
	@Override
	@Bean
	protected UserDetailsService userDetailsService() {
		
		UserDetails thisUserBuilder = User.builder()
			.username("thiagojacinto")
			.password(passwordEncoder.encode("password"))
			.roles(ApplicationUserRole.USER.name()) 	// ROLE_USER
			.build();
		
		// creating an ADMIN role of User
		UserDetails lindaUser = User.builder()
			.username("linda")
			.password(passwordEncoder.encode("password1234"))
			.roles(ApplicationUserRole.ADMIN.name()) // ROLE_ADMIN
			.build();
		
		return new InMemoryUserDetailsManager(
				thisUserBuilder,
				lindaUser
				);
	}

}
