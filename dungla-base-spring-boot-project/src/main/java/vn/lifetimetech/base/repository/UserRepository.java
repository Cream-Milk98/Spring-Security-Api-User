package vn.lifetimetech.base.repository;

import org.springframework.data.repository.CrudRepository;
import vn.lifetimetech.base.model.UsersEntity;

public interface UserRepository extends CrudRepository<UsersEntity, Integer> {

    UsersEntity findById(int id);

    UsersEntity findByUsername(String username);

}
