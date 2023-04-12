package com.example.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dao.DepartmentDao;
import com.example.dao.PostDao;
import com.example.dao.UserDao;
import com.example.dao.YardDao;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
public class PostControllerTests {

        private UserDao userDao;

        private YardDao yardDao;

        private DepartmentDao departmentDao;

       
        
}
