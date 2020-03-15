package br.securityjava.securitylearn.user;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	private static final List<User> USERS = Arrays.asList(
			new User(1, "Thiago Jacinto"),
			new User(2, "Elisabeth Dequeen"),
			new User(3, "Victor Hugo")
			
			);
	
	@GetMapping(path = "/{userId}")
	public User getStUser(@PathVariable("userId") Integer userId) {
		return USERS.stream()
				.filter(user -> userId.equals(user.getUserId()))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("User with ID `" + userId + "` does NOT exists."));
	}
	
}
