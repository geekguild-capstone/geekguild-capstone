package com.geekguild.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "comments")
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "snippet" , length = 6000)
    private String snippet;


    //    Many to One Posts
    @ManyToOne
    @JoinColumn (name = "post_id")
    @JsonIgnoreProperties("comments")
    private Post post;

    //    Many to One User
    @ManyToOne
    @JoinColumn (name = "creator_id")
    private User user;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
    @JsonIgnore
    private List<Reaction> reactions;


}