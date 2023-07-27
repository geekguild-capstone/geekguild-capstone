package com.geekguild.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.scheduling.config.Task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String groupname;

    @Column
    private String image;

    @Column
    private String banner;

    @Column
    private String description;

    @Column
    private Long adminId;

    public Group(long id, String groupname, String image, String banner, String description, Long adminId) {
        this.id = id;
        this.groupname = groupname;
        this.image = image;
        this.banner = banner;
        this.description = description;
        this.adminId = adminId;

    }

    @ManyToMany
    @JoinTable(name = "groupusers",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members = new HashSet<>();

//@ManyToMany(mappedBy = "groupsList")
//private List<User> users;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<Post> posts;



}