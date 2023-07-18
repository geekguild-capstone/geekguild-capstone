package com.geekguild.models;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "Portfolio")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String about;

    @Column(nullable = false)
    private String proj1;

    @Column(nullable = false)
    private String proj2;

    @Column(nullable = false)
    private String proj3;

    @Column(nullable = false)
    private String linkedin;

    @Column(nullable = false)
    private String facebook;

    @Column(nullable = false)
    private String github;

    @Column(nullable = false)
    private String misclink;


    public Portfolio(long id, String about, String proj1, String proj2, String proj3, String linkedin, String facebook, String github, String misclink) {
        this.id = id;
        this.about = about;
        this.proj1 = proj1;
        this.proj2 = proj2;
        this.proj3 = proj3;
        this.linkedin = linkedin;
        this.facebook = facebook;
        this.github = github;
        this.misclink = misclink;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}

