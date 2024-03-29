package com.lavendor.mealmate;

import com.lavendor.mealmate.user.User;
import com.lavendor.mealmate.user.UserDTO;
import com.lavendor.mealmate.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MealmateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MealmateApplication.class, args);
	}

	@Bean
	@Profile("!test")
	CommandLineRunner commandLineRunner(UserService userService){
		return ergs -> {
			User user = userService.createUser(new UserDTO("qqq","qqq", "qqq"));
			if(user != null) userService.addStarterDataToUser(user);

		};
	}
}
