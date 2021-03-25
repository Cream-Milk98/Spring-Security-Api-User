package com.phuc.demo.service.dao;

import com.phuc.demo.entities.CustomUserDetails;
import com.phuc.demo.entities.Users;
import com.phuc.demo.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl{

    @Autowired
    private EntityManager entityManager;

    @Autowired
    IUsersRepository ur;

    public List<Users> findUsers() {
        return (List<Users>) ur.findAll();
    }

    public void saveUser(Users u) {
        ur.save(u);
    }
    public void removeById(int id) {
        ur.deleteById(id);
    }

    public Optional<Users> findById(int id) {
        return ur.findById(id);
    }

    @Transactional
    public UserDetails loadUserById(int id) {
        Users user = ur.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );
        return new CustomUserDetails(user);
    }

    public Users findUserByName(String userName) {
        return ur.findUsersByUserName(userName);
    }

}
