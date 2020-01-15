package com.rohitsalecha.springular.devops.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rohitsalecha.springular.devops.entities.User;
import com.rohitsalecha.springular.devops.repositories.UserRepository;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
    	logger.info("getUsers Called");
        return (List<User>) userRepository.findAll();
    }

    @PostMapping("/users")
    void addUser(@RequestBody User user) {
    	logger.info("User "+user.getName()+" added to Database");
        userRepository.save(user);
    }
}
