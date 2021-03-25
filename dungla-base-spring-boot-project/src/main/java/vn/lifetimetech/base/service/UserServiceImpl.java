package vn.lifetimetech.base.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.lifetimetech.base.model.UsersEntity;
import vn.lifetimetech.base.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UsersEntity getUserById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void seedAdminAccount() {
        UsersEntity adminUser = new UsersEntity();
        adminUser.setUsername("admin");
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        adminUser.setPassword(passwordEncoder.encode("123456"));
        adminUser.setEmail("test@gmail.com");
        adminUser.setFullname("Admin");
        adminUser.setRole("ROLE_ADMIN");
        userRepository.save(adminUser);
    }

}
