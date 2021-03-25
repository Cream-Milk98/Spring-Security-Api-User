package vn.lifetimetech.base.service;

import vn.lifetimetech.base.model.UsersEntity;

public interface UserService {

    UsersEntity getUserById(int id);

    void seedAdminAccount();
}
