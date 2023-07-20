package com.geekguild.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "friend_request")

public class FriendRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id") // Assuming that "receiver_id" is the foreign key column in the friend_request table
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "sender_id") // Assuming that "sender_id" is the foreign key column in the friend_request table
    private User sender;

    @Column(name = "status")
    private String status;


//    public FriendRequest(long id) {
//        this.id = id;
//    }





}
