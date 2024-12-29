package com.miniproject.ENTITY;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Etudiant {
    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String dateNaissance; // Format: YYYY-MM-DD
    private String email;
    private String promotion;


    public Etudiant(int id, String matricule, String nom, String prenom, LocalDate dateNaissance, String email, String promotion) {
    }
}
