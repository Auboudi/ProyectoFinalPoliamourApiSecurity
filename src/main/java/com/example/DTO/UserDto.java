package com.example.DTO;

import java.io.Serializable;
import java.util.List;

import com.example.entities.Department;
import com.example.entities.Yard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto implements Serializable {

    private long id;
    private String name;
    private String surnames;
    private String city;
    private List<String> hobbie;
    private String imageUser;
    private List <Yard> yards;
    private Department department; 


}
