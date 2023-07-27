package com.geekguild.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

//    @Column(name = "username", nullable = false, unique = true)
//    private String username;

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


    public User(long id, String firstname, String lastname, String email, String password, String image, String banner) {
        this.id = id;
//        this.username = userName;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.image = image;
        this.banner = banner;
    }


    public User(User copy) {
        id = copy.id; // This line is SUPER important! Many things won't work if it's absent
        email = copy.email;
        firstname = copy.firstname;
        lastname = copy.lastname;
//        username = copy.username;
        password = copy.password;
        image = copy.image;
        banner = copy.banner;
    }

//    Relationships

    // Each user should have exactly one portfolio, and each profile should be associated with only one user. This allows you to keep the user-specific information, preferences, and settings separate from the core user authentication and authorization data.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Portfolio portfolio;

    // In this scenario, each user can have multiple posts, but each post belongs to only one user. This is the most straightforward relationship for a social media website, where users can create and own their posts.
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Post> posts;

    // Each user should have exactly one work categories, and each work source should be associated with only one user. This allows you to keep the user-specific information, preferences, and settings separate from the core user authentication and authorization data.
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Work work;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Comments> comments;

    // friends
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender")
    List<FriendRequest> friends = new ArrayList<>();

    // groups
    @ManyToMany(mappedBy = "members")
    private Set<Group> groups = new HashSet<>();


    // In this scenario, each user can have multiple languages,
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_languages",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"))
    private List<Language> language;


//    @ManyToMany
//    @JoinTable(name = "groupusers",
//            joinColumns = {@JoinColumn(name = "user_id")},
//    inverseJoinColumns = {@JoinColumn(name = "group_id")})
//    private List<Group> groupsList;
}
