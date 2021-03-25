package com.phuc.demo.controller;

import com.phuc.demo.entities.Blog;
import com.phuc.demo.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class BlogController {
    @Autowired
    IBlogService bs;
    @PostMapping("/blog/add")
    public Blog addBlog(@RequestBody Blog blog){
        bs.AddBlog(blog);
        return  blog;
    }

    @RequestMapping("/blog")
    public List<Blog> View(){
        return  bs.FindAllBlog();
    }
}
