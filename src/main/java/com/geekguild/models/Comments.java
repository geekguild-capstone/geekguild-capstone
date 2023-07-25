package com.geekguild.models;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "comments")
public class Comments {
//    comment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String comments;

    public Comments(long id, String comments) {
        this.id = id;
        this.comments = comments;

    }

//    Many to One Users
    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;


    //    Many to One Posts
    @ManyToOne
    @JoinColumn (name = "post_id")
    private Post post;




}