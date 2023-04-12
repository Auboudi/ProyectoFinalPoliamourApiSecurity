package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dao.DepartmentDao;
import com.example.dao.YardDao;
import com.example.entities.Department;

import com.example.entities.Role;
import com.example.entities.User;
import com.example.entities.Yard;
import com.example.services.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTests {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentDao departmentDao;

    @Autowired
    private YardDao yardDao;


    private User user0;

    private Department dpto0;

    private Yard yard0;

    @BeforeEach
    void setUp() {
        
        dpto0 = Department.builder()
            .name("Dpto0")
            .build();

        yard0 = Yard.builder()
            .name("Yard0")
            .build();

        List<Yard> yards0 = new ArrayList<>();

        yards0.add(yard0);


        user0 = User.builder()

            .name("Test User0")
            .surnames("Test User 0")
            .email("correoTest0@gmail.com")
            .password("password")
            .department(dpto0)
            .city("Murcia")
            .yards(yards0)
            .role(Role.USER)
            .build();




    }

    @Test
    @DisplayName("Test para agregar un user")
    public void testAddUser(){

       Department dpto1 = Department.builder()
            .name("Dpto1")
            .build();

        departmentDao.save(dpto1);

        Yard yard1 = Yard.builder()
            .name("Yard0")
            .build();
        
            yardDao.save(yard1);

        List<Yard> yards1 = new ArrayList<>();

        yards1.add(yard1);

        
        User user1 = User.builder()

            .name("Test User1")
            .surnames("Test User 1")
            .email("correoTest10@gmail.com")
            .password("password")
            .department(dpto1)
            .city("Murcia")
            .yards(yards1)
            .role(Role.USER)
            .build();

        User userAdd = userRepository.save(user1);

        assertThat(userAdd).isNotNull();
        assertThat(userAdd.getId()).isGreaterThan(0L);




    }




    

}
