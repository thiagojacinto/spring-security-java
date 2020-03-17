package br.securityjava.securitylearn.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@GetMapping
	public List<User> getAllUsers() {
		
		// TODO real implementation
		
		System.out.println("getAllUsers()");
		
		return USERS;
	}
	
	@PostMapping
	public void registerUser(@RequestBody User user) {
		System.out.println(user);	// Placeholder
		System.out.println("registerUser()");
	}
	
	@DeleteMapping(path = "{userId}")
	public void deleteUser(@PathVariable("userId") Integer userId) {
		System.out.println("Delete: ID = " + userId);	// Placeholder
		System.out.println("deleteUser()");
	}
	
	@PutMapping(path = "{userId}")
	public void updateUser(@PathVariable("userId") Integer userId, @RequestBody User updatedUser ) {
		System.out.println(String.format("ID = %s; Updated = %s", userId, updatedUser));	// Placeholder
		System.out.println("updateUser()");
	}

}
