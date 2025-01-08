package com.miniproject.ENTITY;
import com.miniproject.ENTITY.Etudiant;


import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Data


public class Module {

    private int id;
    private String nomModule;
    private String codeModule;
    private Professeur professeur; // Foreign key to Professeur (1:1 relationship)
    private LocalDate deadline;    // New field for module deadlines




    // Méthode pour obtenir le nom complet du professeur
    public String getProfessorFullName() {
        if (professeur != null && professeur.getUtilisateur() != null) {
            return professeur.getUtilisateur().getNom() + " " + professeur.getUtilisateur().getPrenom();
        }
        return "N/A";
    }


}
