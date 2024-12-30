package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Module {

    private int id;
    private String nomModule;
    private String codeModule;
    private Professeur professeur; // Foreign key to Professeur (1:1 relationship)
    //to get the whole name of the prof
    public String getProfessorFullName() {
        if (professeur != null && professeur.getUtilisateur() != null) {
            return professeur.getUtilisateur().getNom() + " " + professeur.getUtilisateur().getPrenom();
        }
        return "N/A";
    }
}
