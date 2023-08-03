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

    @Column(name = "proj1link")
    private String proj1link;

    @Column(name = "proj1desc")
    private String proj1desc;

    @Column(name = "proj2")
    private String proj2;

    @Column(name = "proj2link")
    private String proj2link;

    @Column(name = "proj2desc")
    private String proj2desc;

    @Column(name = "proj3")
    private String proj3;

    @Column(name = "pro31link")
    private String proj3link;

    @Column(name = "proj3desc")
    private String proj3desc;

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

    public Portfolio(long id, String about, String proj1, String proj1desc, String proj1link, String proj2, String proj2desc, String proj2link, String proj3, String proj3desc, String proj3link, String linkedin, String facebook, String github, String misclink, String headline) {
        this.id = id;
        this.about = about;
        this.proj1 = proj1;
        this.proj1link = proj1link;
        this.proj1desc = proj1desc;
        this.proj2 = proj2;
        this.proj2link = proj2link;
        this.proj2desc = proj2desc;
        this.proj3 = proj3;
        this.proj3link = proj3link;
        this.proj3desc = proj3desc;
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

