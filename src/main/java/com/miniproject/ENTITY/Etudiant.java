package com.miniproject.ENTITY;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Etudiant {

    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String email;
    private String promotion; // Promotion year or group
}
