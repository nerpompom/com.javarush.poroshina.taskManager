package com.javarush.poroshina.taskManager.util;
//ВРЕМЕННЫЙ КЛАСС

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashGenerator {

    public static void main(String[] args) {
        PasswordEncoder encoder =
                new BCryptPasswordEncoder();

        System.out.println(
                encoder.encode("password123")
        );

        System.out.println(
                encoder.encode("qwerty")
        );

        System.out.println(
                encoder.encode("пароль")
        );

        System.out.println(
                encoder.encode("gift")
        );
    }
}
