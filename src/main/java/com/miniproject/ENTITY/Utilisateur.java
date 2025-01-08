package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Utilisateur {

    private int id;
    private String username;
    private String password;
    private String role;
    private String nom;
    private String prenom;
}
