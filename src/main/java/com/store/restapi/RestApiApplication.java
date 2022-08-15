package com.store.restapi;

import com.store.restapi.security.domain.model.Role;
import com.store.restapi.security.domain.model.Account;
import com.store.restapi.security.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class RestApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApiApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(AccountService userService) {
		userService.saveRole(new Role("ROLE_ADMIN", "Администратор"));
		userService.saveRole(new Role("ROLE_STAFF", "Сотрудник"));
		userService.saveRole(new Role("ROLE_USER", "Пользователь"));

		Account user = new Account("user@mail.net", "password", "Ivanov Ivan");
		user.setEnabled(true);
		userService.createUser(user);
		user = userService.addRoleToUser(user.getEmail(), "ROLE_STAFF");

		Account finalUser = user;
		log.info("User password {}", finalUser.getPassword());
		return args -> {
//			userService.saveUser(finalUser);
		};
	}

}
