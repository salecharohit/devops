package com.rohitsalecha.springular.devops.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.rohitsalecha.springular.devops.entities.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{}
