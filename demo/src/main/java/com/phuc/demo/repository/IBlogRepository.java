package com.phuc.demo.repository;

import com.phuc.demo.entities.Blog;
import org.springframework.data.repository.CrudRepository;

public interface IBlogRepository extends CrudRepository<Blog, Integer> {

}
