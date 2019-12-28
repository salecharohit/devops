package com.rohitsalecha.springular.devops;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.rohitsalecha.springular.devops.entities.User;
import com.rohitsalecha.springular.devops.repositories.UserRepository;

@SpringBootApplication
public class DevopsApplication implements CommandLineRunner {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private UserRepository userRepository;
	
	private ArrayList<User> users = new ArrayList<User>();
	
	public static void main(String[] args) {
		SpringApplication.run(DevopsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		users.add(new User("Goku","goku@dbz.com"));
		users.add(new User("Vegeta","vegeta@dbz.com"));
		users.add(new User("Gohan","gohan@dbz.com"));
		users.add(new User("Trunks","trunks@dbz.com"));
		
		for (User user : users) {
			userRepository.save(user);
			logger.info("User " + user.getName()+ "added");
		}
		
	}

}
