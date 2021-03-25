package com.phuc.demo.service.dao;

import com.phuc.demo.entities.Role;
import com.phuc.demo.entities.Users;
import com.phuc.demo.repository.IRoleRepsitory;
import com.phuc.demo.repository.IUsersRepository;
import com.phuc.demo.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class RoleDaoImpl implements IRoleService {

    @Autowired
    IRoleRepsitory ir;
    @Autowired
    UsersServiceImpl iu;
    @Override
    public void addRole(Role r) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = "";
        if (principal instanceof UserDetails) {
            userName = ((UserDetails)principal).getUsername();
        } else {
            userName = principal.toString();
        }
        ir.save(r);
        Users u = iu.findUserByName(userName);
        u.setRole(r);
        iu.saveUser(u);
    }
}
