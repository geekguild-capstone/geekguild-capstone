package com.geekguild.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "snippet", length = 6000)
    private String snippet;


//    USER RELATIONSHIPS
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn (name = "group_id")
    private Group group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("post") // Add this annotation to avoid circular references
    private List<Comments> comments;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post")
    private List<Reaction> reactions;
}
