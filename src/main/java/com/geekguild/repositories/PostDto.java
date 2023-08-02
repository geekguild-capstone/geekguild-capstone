package com.geekguild.repositories;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private long id;
    private String image;
    private String body;
    private String snippet;
    // Other fields if needed
}
