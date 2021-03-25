package com.phuc.demo.service;

import com.phuc.demo.entities.Blog;
import com.phuc.demo.repository.IBlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBlogService {

    public void AddBlog(Blog blog);

    public List<Blog> FindAllBlog();
}
