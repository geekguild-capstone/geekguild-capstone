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

    @Column(nullable = false)
    private String working;

    @Column(nullable = false)
    private String ask;

    @Column(nullable = false)
    private String learn;

    @Column(nullable = false)
    private String fact;

    @Column(nullable = false)
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
