package com.example.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.entities.User;

public interface UserService {
    public List<User> findAll(Sort sort);

    public Page<User> findAll(Pageable pageable);

    public User findbyId(long id);

    public User save(User user);

    public void delete(User user);

    // service security

    public User add(User user);

    public List<User> findAll();

    public void deleteByEmail(String email);

    public User findByEmail(String email);

    public User update(User user);
}
