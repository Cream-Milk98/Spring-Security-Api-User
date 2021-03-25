package com.phuc.demo.repository;

import com.phuc.demo.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsersRepository extends JpaRepository<Users, Integer> {
    Users findUsersByUserName(String userName);
}
