package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.DTO.UserDto;
import com.example.entities.User;

public interface UserService {
    public List <User> findAll(Sort sort);
    public Page <User> findAll (Pageable pageable);
    public User findbyId (long id); 
    public User save (User user);
    public void delete (User user); 
    
    //service security
    
    User add(User user);
    List<User> findAll();
    void deleteByEmail(String email);
    User findByEmail(String email);
    User update(User user);
}
