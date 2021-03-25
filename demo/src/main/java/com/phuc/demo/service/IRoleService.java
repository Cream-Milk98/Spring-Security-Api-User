package com.phuc.demo.service;

import com.phuc.demo.entities.Role;
import org.springframework.stereotype.Service;

@Service
public interface IRoleService {
    public void addRole(Role r);
}
