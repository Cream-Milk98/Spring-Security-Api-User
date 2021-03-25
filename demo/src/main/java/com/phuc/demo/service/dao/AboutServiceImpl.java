package com.phuc.demo.service.dao;

import com.phuc.demo.entities.About;
import com.phuc.demo.repository.AboutRepository;
import com.phuc.demo.service.IAboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AboutServiceImpl implements IAboutService {

    @Autowired
    private AboutRepository ar;

    public List<About> getAboutAll(){
        return (List<About>)ar.findAll();
    }

    public Optional<About> findAboutById(int id){
        return ar.findById(id);
    }

    public void saveAbout(About a){
        ar.save(a);
    }

    public void saveAbouts(List<About> data){
        ar.saveAll(data);
    }

    public void deleteAboutById(int id){
        ar.deleteById(id);
    }

    public  void deleteAbout(About a){
        ar.delete(a);
    }
}
