package br.securityjava.securitylearn.jwt;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	
	// Inject JwtConfig
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;

	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
					JwtConfig jwtConfig,
					SecretKey secretKey) {
		this.authenticationManager = authenticationManager;
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, 
			HttpServletResponse response)
					throws AuthenticationException {

		try {

			UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
					.readValue(request.getInputStream(), 
							UsernameAndPasswordAuthenticationRequest.class);

			Authentication authentication = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), 
					authenticationRequest.getPassword()
					);

			// Check if username exists, and if so, test the password.
			Authentication authenticate = authenticationManager.authenticate(authentication);

			return authenticate;

		} catch (IOException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		
//		String key = "verysecureverysecureverysecureverysecureverysecure";
		// Now using JwtConfig class + JwtSecretKey class
		
		String token = Jwts.builder()
				.setSubject(authResult.getName())
				.claim("authorities", authResult.getAuthorities())
				.setIssuedAt(new java.util.Date())
				.setExpiration(Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
				.signWith(secretKey)
				.compact();
				
		response.addHeader(
				jwtConfig.getAuthorizationHeader(), 
				jwtConfig.getTokenPrefix() + token);
		}
}
