package br.securityjava.securitylearn.security;

import java.util.Set;
import com.google.common.collect.Sets;

import static br.securityjava.securitylearn.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
	
	USER(Sets.newHashSet()),
	ADMIN(Sets.newHashSet(PRODUCT_READ, PRODUCT_WRITE, USER_READ, USER_WRITE)); 
	
	private final Set<ApplicationUserPermission> permissions;

	private ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}
	
	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}

}
