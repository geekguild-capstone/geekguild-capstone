package com.geekguild.models;

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
    @Table(name = "language")
    public class Language {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(nullable = false)
        private String languageName;

        @Column
        private String logo;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;



        @ManyToMany(mappedBy = "language")
        private List<User> users;




        public Language(long id, String languageName, String logo) {
            this.id = id;
            this.languageName = languageName;
            this.logo = logo;
        }



}
