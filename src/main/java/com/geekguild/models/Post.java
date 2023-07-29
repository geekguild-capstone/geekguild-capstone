package com.geekguild.models;

import com.geekguild.repositories.PostRepository;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "image")
    private String image;

    @Column(nullable = false)
    private String body;

    @Column(name = "snippet")
    private String snippet;


//    USER RELATIONSHIPS
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn (name = "group_id")
    private Group group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Comments> comments;

    // Add the many-to-many relationship with reactions
    @ManyToMany(mappedBy = "posts")
    private List<Reaction> reactions;
}
