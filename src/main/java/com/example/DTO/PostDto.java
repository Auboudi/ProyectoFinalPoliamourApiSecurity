package com.example.DTO;

import java.io.Serializable;

import com.example.entities.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class PostDto implements Serializable {
    private long id;
    private String text;
    private String imagePost;
  



    
}
