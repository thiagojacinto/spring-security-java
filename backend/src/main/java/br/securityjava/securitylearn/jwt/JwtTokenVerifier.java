package br.securityjava.securitylearn.jwt;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.base.Strings;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JwtTokenVerifier extends OncePerRequestFilter {
	
	// JwtConfig files
	
	private final JwtConfig jwtConfig;
	private final SecretKey secretKey;
	
	// Constructor
	public JwtTokenVerifier(JwtConfig jwtConfig, SecretKey secretKey) {
		this.jwtConfig = jwtConfig;
		this.secretKey = secretKey;
	}

	/*
	 * Invoke THIS filter just ONCE for every single request from the client.
	 * 
	 * That's what extedending from `Once per request filter` does.
	 */
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader(this.jwtConfig.getAuthorizationHeader());
		
		// Verify if header is null or does NOT have `Bearer` at begining
		if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(this.jwtConfig.getTokenPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		// Replaces `Bearer` for empty, from the token input header
		String token = authorizationHeader.replace(this.jwtConfig.getTokenPrefix(), "");
		
		try {
			
			
			Jws<Claims> claimsJws = Jwts.parserBuilder()
					.setSigningKey(this.secretKey)
					.build()
					.parseClaimsJws(token);
			
			Claims body = claimsJws.getBody();
			String username = body.getSubject();
			
			// creates a list of every `authority` inside body:
			@SuppressWarnings("unchecked")
			var authorities = (List<Map<String, String>>) body.get("authorities");
			
			// populate a list of GrantedAuthorities with it
			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
				.map(item -> new SimpleGrantedAuthority(item.get("authority")))
				.collect(Collectors.toSet());
			
			// then creates an authentication with this:
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username, 
					null,
					simpleGrantedAuthorities);
					
			// Authenticate the client that does all the above things
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		} catch (JwtException e) {
			// Alerts that this token is INVALID
			throw new IllegalStateException(String.format("Token ( %s ) cannot be trusted.", token)); 
		}
		
		filterChain.doFilter(request, response);
		
	}

}
