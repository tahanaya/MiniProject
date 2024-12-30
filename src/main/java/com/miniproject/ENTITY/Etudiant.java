package com.miniproject.ENTITY;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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
