package com.example.services;

import static org.mockito.BDDMockito.given;

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
import com.example.entities.Post;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.entities.Yard;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest
public class PostServiceTests {

    @Mock
    private UserDao userDao;

    @Mock
    private YardDao yardDao;

    @Mock
    private DepartmentDao departmentDao;

    @Mock
    private PostDao postDao;

    @InjectMocks
    private PostServiceImpl postService;

    private User user;

    private Department department;

    private Yard yard;

    private Post post;
    
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

        post = Post.builder()
        .id(100L)
        .text("Post Tests")
        .user(user)
        .build();

    }

    @Test
    @DisplayName("Test de servicio para persistir un post")
    public void testGuardarPost() {
        // Given

        departmentDao.save(department);
        yardDao.save(yard);
        userDao.save(user);

        given(postDao.save(post)).willReturn(post);

        // When

        Post postGuardado = postService.save(post);

        // Then

        assertThat(postGuardado).isNotNull();


    }

    @DisplayName("Recupera una lista vacía de posts")
    @Test
    public void testEmptyPostList() {

        // Given

        given(postDao.findAll()).willReturn(Collections.emptyList());

        // When

        List<Post> posts = postDao.findAll();

        // Then

        assertThat(posts).isEmpty();

    }



}
