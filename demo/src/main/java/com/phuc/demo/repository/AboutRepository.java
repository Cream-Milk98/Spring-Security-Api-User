package com.phuc.demo.repository;

import com.phuc.demo.entities.About;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AboutRepository extends CrudRepository<About, Integer> {

}
