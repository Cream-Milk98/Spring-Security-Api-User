package com.phuc.demo.controller;

import com.phuc.demo.entities.About;
import com.phuc.demo.service.IAboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class DemoController {

    @Autowired
    IAboutService as;

    @RequestMapping("/about")
    public List<About> getAll(){
        return as.getAboutAll();
    }

    @GetMapping("/about/{id}")
    public Optional<About> getOne(@PathVariable("id") int id) {
        Optional<About> a = as.findAboutById(id);
        return a;
    }

    @PostMapping("/add")
    public About add(@RequestBody About a) {
        System.out.println("Add: "+a);
        as.saveAbout(a);
        return a;

    }

    @DeleteMapping("/remove/{id}")
    public void deleteById(@PathVariable("id") int id) {
       as.deleteAboutById(id);
        System.out.println("Delete: "+id);
    }

    @PutMapping("/update")
    public About update(@RequestBody About a) {
        as.saveAbout(a);
        return a;
    }

}
