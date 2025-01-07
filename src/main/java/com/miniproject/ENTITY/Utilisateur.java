package com.miniproject.ENTITY;

import lombok.Data;

import java.util.List;

@Data
public class Utilisateur {

    private int id;
    private String username;
    private String password;
    private String role; // Possible roles: "ADMINISTRATEUR", "SECRETAIRE", "PROFESSEUR"
    private String nom;
    private String prenom;

    private List<Notification> notifications;     //  list of notifications associated with the user


}

