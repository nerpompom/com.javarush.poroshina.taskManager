package com.javarush.poroshina.taskManager.model;

import jakarta.persistence.*;

import java.util.List;

//@Entity
//@Table(name = "users")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    long id;
//
//    @Column(nullable = false, unique = true)
//    String username;
//
//    @Column(nullable = false)
//    String password;
//
//    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Task> tasks;
//
//    @OneToMany(mappedBy = "executor", fetch = FetchType.LAZY)
//    private List<Task> executedTasks;
//}
