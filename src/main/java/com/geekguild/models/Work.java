package com.geekguild.models;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Entity
@Table(name = "work")
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "working")
    private String working;

    @Column(name = "ask")
    private String ask;

    @Column(name = "learn")
    private String learn;

    @Column(name = "fact")
    private String fact;

    @Column(name = "help")
    private String help;

    public Work(long id, String working, String ask, String learn, String fact, String help) {
        this.id = id;
        this.working = working;
        this.ask = ask;
        this.learn = learn;
        this.fact = fact;
        this.help = help;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
