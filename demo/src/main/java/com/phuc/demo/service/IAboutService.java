package com.phuc.demo.service;

import com.phuc.demo.entities.About;
import com.phuc.demo.repository.AboutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IAboutService {

    public List<About> getAboutAll();

    public Optional<About> findAboutById(int id);

    public void saveAbout(About a);

    public void saveAbouts(List<About> data);

    public void deleteAboutById(int id);

    public  void deleteAbout(About a);
}
