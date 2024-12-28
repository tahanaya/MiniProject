package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Etudiant {
    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String dateNaissance; // Format: YYYY-MM-DD
    private String email;
    private String promotion;
}
