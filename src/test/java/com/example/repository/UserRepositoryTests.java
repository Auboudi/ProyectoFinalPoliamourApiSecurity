package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.dao.DepartmentDao;
import com.example.dao.YardDao;
import com.example.entities.Department;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.entities.Yard;
import com.example.services.UserRepository;

// @SpringBootTest
@DataJpaTest
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
    public void testAddUser() {

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

                .name("Test User4")
                .surnames("Test User 3")
                .email("correoTest40@gmail.com")
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

    @DisplayName("Test para listar usuarios")
    @Test
    public void testFindAllUsers() {

        // Given

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

                .name("Test User2")
                .surnames("Test User 2")
                .email("correoTest2@gmail.com")
                .password("password")
                .department(dpto1)
                .city("Murcia")
                .yards(yards1)
                .role(Role.USER)
                .build();

        departmentDao.save(dpto0);
        yardDao.save(yard0);

        userRepository.save(user0);
        userRepository.save(user1);

        // When

        List<User> usuarios = userRepository.findAll();

        // Then

    assertThat(usuarios).isNotNull();
    assertThat(usuarios).size().isEqualTo(14);

    }

    @Test
    @DisplayName("Test para recuperar un usuario por ID")
    public void testFindByUserId() {

        // Given

        departmentDao.save(dpto0);


        userRepository.save(user0);

        // When

        User user = userRepository.findById(user0.getId()).get();

        // Then

        assertThat(user.getId()).isNotEqualTo(0L);

    }

    @Test
    @DisplayName("Test para actualizar un user")
    public void testUpdateUser() {

        // Given

        departmentDao.save(dpto0);
        userRepository.save(user0);

        // When

        User userGuardado = userRepository.findByEmail(user0.getEmail()).get();

        userGuardado.setName("UPDATE");
        userGuardado.setSurnames("UPDATE");
        userGuardado.setEmail("email@email.com");

        User userUpdated = userRepository.save(userGuardado);

        // Then

        assertThat(userUpdated.getEmail()).isEqualTo("email@email.com");
        assertThat(userUpdated.getName()).isEqualTo("UPDATE");
        assertThat(userUpdated.getSurnames()).isEqualTo("UPDATE");

    }

    @DisplayName("Test para eliminar un user")
    @Test
    public void testDeleteUser() {

        // Given

        departmentDao.save(dpto0);
        yardDao.save(yard0);

        userRepository.save(user0);

        // When 

        userRepository.delete(user0);
        Optional<User> optionalUser = userRepository.findByEmail(user0.getEmail());

        // Then 

        assertThat(optionalUser).isEmpty();



    }








    

}
