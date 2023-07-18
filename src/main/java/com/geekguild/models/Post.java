package com.geekguild.models;

import com.geekguild.repositories.PostRepository;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "post")
public class Post {
    @Column(name = "image")
    private String image;

    @Column(nullable = false)
    private String body;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
