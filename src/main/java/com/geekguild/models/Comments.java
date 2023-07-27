package com.geekguild.models;


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

    public Comments(long id, String text) {
        this.id = id;
        this.text = text;

    }

    //    Many to One Posts
    @ManyToOne
    @JoinColumn (name = "post_id")
    private Post post;

    //    Many to One User
    @ManyToOne
    @JoinColumn (name = "creator_id")
    private User user;

    @ManyToMany(mappedBy = "comments")
    private List<Reaction> reactions;

}