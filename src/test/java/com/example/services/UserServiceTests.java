package com.example.services;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.example.entities.Department;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.entities.Yard;

import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
public class UserServiceTests {
    
    @Mock
    private UserDao userDao;

    @Mock
    private DepartmentDao departmentDao;

    @Mock
    private YardDao yardDao;
  

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    private Department department;

    private Yard yard;

    private List<Yard> yards = new ArrayList<>();
    
    @BeforeEach
    void setUp() {

        department = Department.builder()
        .id(10L)
        .name("Dpto0")
        .build();

        yard = Yard.builder()
        .name("Yard0")
        .id(10L)
        .build();



        yards.add(yard);

        user = User.builder()
        .id(100L)
        .name("Test User0")
        .surnames("Test User 0")
        .email("correoTest0@gmail.com")
        .password("password")
        .department(department)
        .city("Murcia")
        .yards(yards)
        .role(Role.USER)
        .build();

    }




    @Test
    @DisplayName("Test de servicio para persisrtir un usuario")
    public void testGuardarUser() {
        // Given

        departmentDao.save(department);
        yardDao.save(yard);



        given(userDao.save(user)).willReturn(user);

        // When

        User userguardado = userService.save(user);

        // Then 

        assertThat(userguardado).isNotNull();

        
    }

    @DisplayName("Recupera una lista vacía de usuarios")
    @Test
    public void testEmptyUserList() {

        // Given

        given(userDao.findAll()).willReturn(Collections.emptyList());

        // When

        List<User> usuarios = userDao.findAll();

        // Then

        assertThat(usuarios).isEmpty();

    }

    





}
