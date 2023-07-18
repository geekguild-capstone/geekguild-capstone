package com.geekguild.models;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "comments")
public class Comments {
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
//    Many to One Posts

}