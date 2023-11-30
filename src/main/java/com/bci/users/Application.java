package com.bci.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Application implements CommandLineRunner {
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String password = "user123";
		System.out.println("bCryptPasswordEncoder for user123" + bCryptPasswordEncoder.encode(password));
		System.out.println("bCryptPasswordEncoder for 01234" + bCryptPasswordEncoder.encode("01234"));
		System.out.println("UUIDs:");
		System.out.println("UUID generado: " + UUID.randomUUID());
		System.out.println("UUID generado: " + UUID.randomUUID());
		System.out.println("UUID generado: " + UUID.randomUUID());
		System.out.println("UUID generado: " + UUID.randomUUID());
		System.out.println("UUID generado: " + UUID.randomUUID());
	}
}
