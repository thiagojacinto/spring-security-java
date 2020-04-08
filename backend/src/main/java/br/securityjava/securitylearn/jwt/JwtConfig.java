package br.securityjava.securitylearn.jwt;

import javax.crypto.SecretKey;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.security.Keys;

@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {
	
	private String secretKey;
	private String tokenPrefix;
	private String tokenExpirationAfterDays;
	
	// Constructor
	public JwtConfig(String secretKey, String tokenPrefix, String tokenExpirationAfterDays) {
		super();
		this.secretKey = secretKey;
		this.tokenPrefix = tokenPrefix;
		this.tokenExpirationAfterDays = tokenExpirationAfterDays;
	}
	
	// Getters and Setters

	public String getSecretKey() {
		return secretKey;
	}

	public String getTokenPrefix() {
		return tokenPrefix;
	}

	public String getTokenExpirationAfterDays() {
		return tokenExpirationAfterDays;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void setTokenPrefix(String tokenPrefix) {
		this.tokenPrefix = tokenPrefix;
	}

	public void setTokenExpirationAfterDays(String tokenExpirationAfterDays) {
		this.tokenExpirationAfterDays = tokenExpirationAfterDays;
	}
	
	// Methods
	public SecretKey getSecretKeyForSigning() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public String getAuthorizationHeader() {
		return HttpHeaders.AUTHORIZATION;
	}

}
