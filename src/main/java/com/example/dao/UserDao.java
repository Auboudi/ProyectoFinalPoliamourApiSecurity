package com.example.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.entities.User;

public interface UserDao extends JpaRepository<User, Long> {

    @Query(value = "select u from User u left join fetch u.department")
    public List<User> findAll(Sort sort);

    @Query(value = "select u from User u left join fetch u.department", countQuery = "select count(u) from User u left join  u.department")

    public Page<User> findAll(Pageable pageable);

    @Query(value = "select u from User u left join fetch u.department where u.id= :id")
    public User findById(long id);

}
