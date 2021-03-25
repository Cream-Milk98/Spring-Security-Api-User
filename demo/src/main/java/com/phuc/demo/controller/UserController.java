package com.phuc.demo.controller;

import com.phuc.demo.entities.CustomUserDetails;
import com.phuc.demo.entities.Role;
import com.phuc.demo.entities.Users;
import com.phuc.demo.jwt.JwtTokenProvider;
import com.phuc.demo.security.LoginResponse;
import com.phuc.demo.service.IRoleService;
import com.phuc.demo.service.dao.UserDetailsServiceImpl;
import com.phuc.demo.service.dao.UsersServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    UserDetailsServiceImpl udsi;

    @Autowired
    private UsersServiceImpl user;

    @RequestMapping(value = {"/", "/user"})
    public List<Users> findUsers() {
        return user.findUsers();
    }

    @RequestMapping("/admin")
    public String viewAmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = "";
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        System.out.println(userName);
        return userName;
    }

    @RequestMapping("/403")
    public String viewErr() {
        return "403";
    }

    @GetMapping("/role")
    public Role findRole() {
        Optional<Users> u = user.findById(9);
        Role r = u.get().getRole();
        return r;
    }

    @PostMapping("/login")
    public LoginResponse authenticateUser(@RequestBody Users a) {
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        a.getUserName(),
                        a.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken(authentication);
        return new LoginResponse(jwt);
    }

    @PostMapping("/user/add")
    public Users add(@RequestBody Users a) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = "";
        if (principal instanceof UserDetails) {
             userName = ((UserDetails)principal).getUsername();
        } else {
             userName = principal.toString();
        }
        System.out.println(userName);
        user.saveUser(a);
        return a;
    }

    @DeleteMapping("/user/remove/{id}")
    public List<Users> Remove(@PathVariable("id") int id){
        user.removeById(id);
        return user.findUsers();
    }

    @PutMapping("/user/update")
    public List<Users> Update(@RequestBody Users a){
        user.saveUser(a);
        return user.findUsers();
    }
}
