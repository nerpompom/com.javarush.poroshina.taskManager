package com.javarush.poroshina.taskManager.conroller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id) {
        return "User with ID: " + id;
    }

    @PostMapping
    public String createUser(@RequestBody String userName) {
        return "Created user: " + userName;
    }
}
