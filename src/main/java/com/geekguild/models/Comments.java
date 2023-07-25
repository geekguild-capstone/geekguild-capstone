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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String text;

    public Comments(long id, String text) {
        this.id = id;
        this.text = text;

    }

    //    Many to One Posts
    @ManyToOne
    @JoinColumn (name = "post_id")
    private Post post;

    //    Many to One Posts
//    @ManyToOne
//    @JoinColumn (name = "user_id")
//    private User user;


}