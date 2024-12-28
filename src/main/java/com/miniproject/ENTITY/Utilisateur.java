package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Utilisateur {
    private int id;
    private String username;
    private String password;
    private String role; // Possible values: "ADMIN", "SECRETAIRE", "PROFESSEUR"
}
