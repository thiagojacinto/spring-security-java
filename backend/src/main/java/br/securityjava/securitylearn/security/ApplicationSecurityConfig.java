package br.securityjava.securitylearn.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.securityjava.securitylearn.auth.ApplicationUserService;
import br.securityjava.securitylearn.jwt.JwtConfig;
import br.securityjava.securitylearn.jwt.JwtTokenVerifier;
import br.securityjava.securitylearn.jwt.JwtUsernameAndPasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)	// To set permission based auth with ANNOTATIONS!
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

	// Attributes
	private final PasswordEncoder passwordEncoder;	// calls encoder to deal with password
	private final ApplicationUserService applicationUserService;
	// JwtConfig injection
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;

	// Constructor
	@Autowired
	public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, 
			ApplicationUserService applicationUserService,
			JwtConfig jwtConfig,
			SecretKey secretKey) {
		
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
		
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
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
		
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()	// This informs that the session will be stateless = JWT way
		
		// Jwt Filters
			// [1] AuthenticationFilter ONE
		.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
			// [2] Token verifier to each request of the previously authenticated client:
		.addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey), JwtUsernameAndPasswordAuthenticationFilter.class)
		
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
		.authenticated();
		
		/*
		 * Authentication methods from this line:
		 * 
		 * 1) BASIC
		 * 2) FORM BASED
		 */
//		.and()
//		.httpBasic();	// used when basic auth is required. (simpler)
		
//		.formLogin()	// using Form based auth (sophisticated)
//		
//			// custom pages: login + successfully login redirects to
//			.loginPage("/login").permitAll()	// change default login page
//			.defaultSuccessUrl("/products", true)	// change default redirect after login success
//			.passwordParameter("password")
//			.usernameParameter("username")
//		
//		// Remember me feature
//		.and()
//		.rememberMe()	// default value = 2 weeks
//			.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
//			.key("somethingverysecured")
//			.rememberMeParameter("remember-me")	// names the cookie of rememberMe feature
//			// MUST be the same of `html`s tag ID & NAME
//			
//		// Clears login sessionId and remember-me cookies
//		.and()
//		.logout()
//			.logoutUrl("/logout")
//			.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))	// just because csrf() is DISABLE, best practice is use POST
//			.clearAuthentication(true)
//			.invalidateHttpSession(true)
//			.deleteCookies("JSESSIONID", "remember-me")
//			.logoutSuccessUrl("/login");
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Using the custom provider at bottom
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		
		provider.setUserDetailsService(applicationUserService);
		
		return provider;
	}

}
