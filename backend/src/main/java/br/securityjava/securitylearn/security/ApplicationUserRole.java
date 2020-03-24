package br.securityjava.securitylearn.security;

import static br.securityjava.securitylearn.security.ApplicationUserPermission.PRODUCT_READ;
import static br.securityjava.securitylearn.security.ApplicationUserPermission.PRODUCT_WRITE;
import static br.securityjava.securitylearn.security.ApplicationUserPermission.USER_READ;
import static br.securityjava.securitylearn.security.ApplicationUserPermission.USER_WRITE;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.google.common.collect.Sets;

public enum ApplicationUserRole {
	
	USER(Sets.newHashSet()),
	ADMIN(Sets.newHashSet(PRODUCT_READ, PRODUCT_WRITE, USER_READ, USER_WRITE)),
	ADMINTRAINEE(Sets.newHashSet(PRODUCT_READ, USER_READ)); 
	
	private final Set<ApplicationUserPermission> permissions;

	private ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}
	
	public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
		
		Set<SimpleGrantedAuthority> permissionsAuthorities =  getPermissions().stream()
			.map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
			.collect(Collectors.toSet());
		
		permissionsAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
		
		return permissionsAuthorities;
	}

}
