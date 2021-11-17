package ee.tlu.forum;

import ee.tlu.forum.model.Post;
import ee.tlu.forum.model.Role;
import ee.tlu.forum.model.Thread;
import ee.tlu.forum.model.User;
import ee.tlu.forum.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean // uncomment and run once to get some test users/roles
//	CommandLineRunner run(UserService userService) {
//		return args -> {
//			userService.saveRole(new Role(null, "ROLE_USER"));
//			userService.saveRole(new Role(null, "ROLE_MODERATOR"));
//			userService.saveRole(new Role(null, "ROLE_ADMIN"));
//
//			userService.saveUser(new User(null,
//					"ree",
//					"reeee",
//					"r",
//					new ArrayList<>(),
//					"ree",
//					"reee",
//					new ArrayList<Thread>(),
//					new ArrayList<Post>()));
//
//			userService.saveUser(new User(null,
//					"raa",
//					"raaaa",
//					"raaa",
//					new ArrayList<>(),
//					"raaa",
//					"raaaaa",
//					new ArrayList<Thread>(),
//					new ArrayList<Post>()));
//
//			userService.saveUser(new User(null,
//					"ooowwwooo",
//					"owowow",
//					"owowowowow",
//					new ArrayList<>(),
//					"owowowwow",
//					"owowowowowo",
//					new ArrayList<Thread>(),
//					new ArrayList<Post>()));
//
//			userService.addRoleToUser("ree", "ROLE_USER");
//			userService.addRoleToUser("raa", "ROLE_ADMIN");
//			userService.addRoleToUser("ooowwwooo", "ROLE_USER");
//			userService.addRoleToUser("ooowwwooo", "ROLE_MODERATOR");
//		};
//	}

}
