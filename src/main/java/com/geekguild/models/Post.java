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
@Table(name = "posts")
public class Post {
    @Column(nullable = false)
    private String title;

    @Column(length = 1024, nullable = false)
    private String body;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}
