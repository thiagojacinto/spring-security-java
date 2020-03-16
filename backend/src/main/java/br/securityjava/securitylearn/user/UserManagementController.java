package br.securityjava.securitylearn.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("management/api/v1/users")
public class UserManagementController {
	
	private static final List<User> USERS = Arrays.asList(
			new User(1, "Thiago Jacinto"),
			new User(2, "Ale Santos"),
			new User(3, "Abdias Nascimento")
			);
	
	// Methods
	
	public List<User> getAllUsers() {
		return USERS;
	}
	
	public void registerUser(User user) {
		
	}

}
