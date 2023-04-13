package com.example.controllers;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import com.example.entities.Department;
import com.example.entities.Role;
import com.example.entities.User;
import com.example.entities.Yard;
import com.example.services.UserService;
import com.example.utilities.FileDownloadUtil;
import com.example.utilities.FileUploadUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;


import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileUploadUtil fileUploadUtil;

    @MockBean
    private FileDownloadUtil fileDownloadUtil;

    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp(){

        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
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
        .email("correoTest1000@poliamor")
        .password("password")
        .department(department)
        .city("Murcia")
        .role(Role.USER)
        .build();

        String jsonStringUser = objectMapper.writeValueAsString(user);


        MockMultipartFile bytesArrayUSer = new MockMultipartFile("user",
                null, "application/json", jsonStringUser.getBytes());

        mockMvc.perform(multipart("/users/add")
                .file("userFile", null)
                .file(bytesArrayUSer))
                .andExpect(status().isUnauthorized())
                .andDo(print());
      

    }

    @DisplayName("Test guardar un usuario con usuario mockeado")
    @Test
    @WithMockUser(username = "admin@poliamor.com", 
                  authorities = {"ADMIN", "USER"})

    void testGuardarUserConUserMocked() throws Exception {
            Department department = Department.builder()
                    .name("Dpto0")
                    .build();    
        Yard yard = Yard.builder()
                .name("Yard")
                .department(department)
                .build();
        List<Yard> yards = new ArrayList<>();

        yards.add(yard);

                
             User user = User.builder()
                    .name("Test User 0")
                    .surnames("Test User 0")
                    .email("correo@poliamor.com")
                    .password("password1")
                    .department(department)
                    .city("Murcia")
                    .yards(yards)
                    .role(Role.USER)
                    .build();

        String jsonStringUser = objectMapper.writeValueAsString(user);

        MockMultipartFile bytesArrayUser = new MockMultipartFile("user",
                null, "application/json", jsonStringUser.getBytes());

        mockMvc.perform(multipart("/users/add")
                .file("fileUser", null)
                .file(bytesArrayUser))
                .andExpect(status().isOk())
                .andDo(print());
    }    

}

