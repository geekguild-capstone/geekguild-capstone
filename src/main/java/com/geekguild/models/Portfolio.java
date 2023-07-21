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

    @Column(name = "about")
    private String about;

    @Column(name = "proj1")
    private String proj1;

    @Column(name = "proj2")
    private String proj2;

    @Column(name = "proj3")
    private String proj3;

    @Column(name = "linkedin")
    private String linkedin;

    @Column(name = "facebook")
    private String facebook;

    @Column(name = "github")
    private String github;

    @Column(name = "misclink")
    private String misclink;

    @Column(name = "headline")
    private String headline;

    public Portfolio(long id, String about, String proj1, String proj2, String proj3, String linkedin, String facebook, String github, String misclink, String headline) {
        this.id = id;
        this.about = about;
        this.proj1 = proj1;
        this.proj2 = proj2;
        this.proj3 = proj3;
        this.linkedin = linkedin;
        this.facebook = facebook;
        this.github = github;
        this.misclink = misclink;
        this.headline = headline;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}

