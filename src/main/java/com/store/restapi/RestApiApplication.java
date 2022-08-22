package com.store.restapi;

import com.store.restapi.account.Role;
import com.store.restapi.account.Account;
import com.store.restapi.account.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
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

		Account account = new Account("user@mail.net", "password", "Ivanov Ivan");
		account.setEnabled(true);
		userService.createAccount(account);
		account = userService.addRoleToAccount(account, "ROLE_STAFF");

		Account finalUser = account;
		log.info("User password {}", finalUser.getPassword());
		return args -> {
//			userService.saveUser(finalUser);
		};
	}

}
