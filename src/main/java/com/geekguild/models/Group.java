//package com.geekguild.models;
//
//
//import jakarta.persistence.*;
//import org.springframework.scheduling.config.Task;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Entity
//@Table(name="groups")
//public class Group {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long id;
//
//    @Column(nullable = false)
//    private String name;
//
//
//    public Group() {
//    }
//
//    public Group(String name) {
//        this.name = name;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//
//
//    //    Working Group to User Relationship
//    @ManyToMany(mappedBy = "groups")
//    private Set<User> members = new HashSet<>();


//    public List<User> getUsers() {
//        return users;
//    }

//    public void setTasks(List<User> users) {
//        this.users = users;
//    }

    //    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
//    List<User> users = new ArrayList<>();



//}
