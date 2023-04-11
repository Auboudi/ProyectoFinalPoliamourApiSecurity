package com.example.services;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {
    
    @Autowired
    private UserRepository userRepository;

    private User user0;

    @BeforeEach
    void setUp() {


        user0 = User.builder()
            .name("Test User0")
            .surnames("Test User 0")
            .email("correoTest0@gmail.com")
            .build();


    }



    

}
