package com.example.controllers;


import static org.mockito.ArgumentMatchers.any;
// Para seguir el enfoque BDD con Mockito
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.entities.Department;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.services.UserRepository;
import com.example.services.UserService;
import com.example.utilities.FileDownloadUtil;
import com.example.utilities.FileUploadUtil;
import com.fasterxml.jackson.databind.ObjectMapper;



@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileUploadUtil fileUploadUtil;

    @MockBean
    private FileDownloadUtil fileDownloadUtil;

    @Autowired
    private WebApplicationContext context;

    private User user;

    private Department department;

    @BeforeEach
    public void setUp(){

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        department = Department.builder().name("Dpto").id(100L).build();

        

        user = User.builder()
            .id(100L)
            .name("Test User0")
            .surnames("Test User 0")
            .email("correoTest1000@gmail.com")
            .password("password")
            .city("Murcia")
            .department(department)
            .role(Role.USER)
            .build();


    }

    @Test
    void testGuardarUser() throws Exception {
        Department department = Department.builder()
        .id(10L)
        .name("Dpto0")
        .build();


        User user = User.builder()
        .id(100L)
        .name("Test User0")
        .surnames("Test User 0")
        .email("correoTest1000@gmail.com")
        .password("password")
        .department(department)
        .city("Murcia")
        .role(Role.USER)
        .build();

        given(userService
        .save(any(User.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
        given(userService.save(any(User.class)))
        .willAnswer(invocation -> invocation.getArgument(0));

        // when

        String jsonStringProduct = objectMapper.writeValueAsString(user);
        System.out.println(jsonStringProduct);
        ResultActions response = mockMvc
                .perform(post("/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringProduct));

        // then
        response.andDo(print())
        .andExpect(status().isUnauthorized());
        

    }


                
        @DisplayName("Test listar usuarios con usuario mockeado")
        @Test
        @WithMockUser(username = "admin@poliamor.com", authorities = {"ADMIN", "USER"})
        void testListarUsuarios() throws Exception {

                mockMvc.perform(MockMvcRequestBuilders.get("/users/admin/all"))
                .andDo(print()).andExpect(status().isOk());
                             
                                               

                        }
        
        }
