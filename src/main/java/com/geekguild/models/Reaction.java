package com.geekguild.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "reaction")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String reaction;

    public Reaction(long id, String reaction) {
        this.id = id;
        this.reaction = reaction;
    }


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("reactions") // Add this annotation to avoid circular references
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    @JsonIgnoreProperties("reactions") // Add this annotation to avoid circular references
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    @JsonIgnoreProperties("reactions") // Add this annotation to avoid circular references
    private Comments comment;


}
