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
@Table(name="groups")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String groupname;

    public Group(long id, String groupname) {
        this.id = id;
        this.groupname = groupname;

    }

    @ManyToMany
    @JoinTable(name = "groupusers",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> members = new HashSet<>();


}