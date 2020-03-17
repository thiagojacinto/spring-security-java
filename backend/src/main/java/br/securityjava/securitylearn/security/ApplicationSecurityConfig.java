package br.securityjava.securitylearn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)	// To set permission based auth with ANNOTATIONS!
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
		
		.csrf()		// Cross sites request forgery: security measure
		.disable()	// disable() -> when consumed by another service
		
		// config the CSRF handling -> when processed by the browser (normal users)
//		.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//		.and()
		
		.authorizeRequests()
		.antMatchers("/", "index", "/css/index.css", "/js/index.js")	// Used to whitelist access permission
		.permitAll()	// then, permits access to predefined Matchers
		.antMatchers("/api/**").hasRole(ApplicationUserRole.USER.name()) 	// Used to protect access ONLY by ROLE_USER
		
		// the ORDER does matter on defining antMatchers
//		.antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(ApplicationUserPermission.PRODUCT_WRITE.getPermission())
//		.antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(ApplicationUserPermission.PRODUCT_WRITE.getPermission())
//		.antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(ApplicationUserPermission.PRODUCT_WRITE.getPermission())
//		.antMatchers("/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())
		
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
//				.roles(ApplicationUserRole.USER.name()) 	// ROLE_USER
				.authorities(ApplicationUserRole.USER.getGrantedAuthorities())
				.build();

		// creating an ADMIN role of User
		UserDetails lindaUser = User.builder()
				.username("linda")
				.password(passwordEncoder.encode("password1234"))
//				.roles(ApplicationUserRole.ADMIN.name()) // ROLE_ADMIN
				.authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
				.build();

		// creating an ADMINTRAINEE role of User
		UserDetails tomUser = User.builder()
				.username("tomhashtag")
				.password(passwordEncoder.encode("password1234"))
//				.roles(ApplicationUserRole.ADMINTRAINEE.name()) // ROLE_ADMINTRAINEE
				.authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
				.build();

		return new InMemoryUserDetailsManager(
				thisUserBuilder,
				lindaUser,
				tomUser
				);
	}

}
