package com.phuc.demo.service.dao;

import com.phuc.demo.entities.Blog;
import com.phuc.demo.repository.IBlogRepository;
import com.phuc.demo.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements IBlogService {
    @Autowired
    IBlogRepository ibr;

    public void AddBlog(Blog blog){
        ibr.save(blog);
    }
    public List<Blog> FindAllBlog(){
        return (List<Blog>) ibr.findAll();
    }
}
