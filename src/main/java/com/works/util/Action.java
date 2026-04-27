package com.works.util;

import java.util.Random;

public class Action {
    Random random = new Random();

    public int add() { return random.nextInt(40) + 60; }
    public int update() { return random.nextInt(40) + 60; }
    public int delete() { return random.nextInt(40) + 60; }

    // email validation function
    public boolean emailValidation(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

}
