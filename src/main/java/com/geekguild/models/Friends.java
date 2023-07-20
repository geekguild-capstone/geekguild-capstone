package com.geekguild.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

@Entity
@Table(name = "friends")

public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "approved")
    private boolean isApproved = false;

    public Friends(long id) {
        this.id = id;
    }

    //    Relationships

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;



}
