package com.geekguild.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "reaction")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String reaction;

    public Reaction(long id, String reaction) {
        this.id = id;
        this.reaction = reaction;
    }


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comments comment;


//    // Add the many-to-many relationship with post
//    @ManyToMany
//    @JoinTable(
//        name = "post_reactions",
//        joinColumns = @JoinColumn(name = "reaction_id"),
//        inverseJoinColumns = @JoinColumn(name = "post_id")
//    )
//    private List<Post> posts;
//
//    // Add the many-to-many relationship with comments
//    @ManyToMany
//    @JoinTable(
//            name = "comment_reactions",
//            joinColumns = @JoinColumn(name = "reaction_id"),
//            inverseJoinColumns = @JoinColumn(name = "comment_id")
//    )
//    private List<Comments> comments;



}
