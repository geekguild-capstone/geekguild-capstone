package com.geekguild.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "image")
    private String image;

    @Column(name = "banner")
    private String banner;


    public User(long id, String userName, String email, String password) {
        this.id = id;
        this.username = userName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }


    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        email = copy.email;
        firstname = copy.firstname;
        lastname = copy.lastname;
        username = copy.username;
        password = copy.password;
    }


//    Relationships

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post> posts;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Portfolio portfolio;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Work work;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comments> comments;




//    Working Group to User Relationship

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    List<FriendRequest> friends = new ArrayList<>();


    // try to add a many to many relationship to keep the db from duplicating new columns.
//@ManyToMany

}
