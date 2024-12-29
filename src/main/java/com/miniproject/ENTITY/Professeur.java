package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Professeur extends Utilisateur {

    private int id;
    private Utilisateur utilisateur; // Foreign key to Utilisateur (1:1 relationship)
    private String specialite;

    public String getNom() {
        return utilisateur.getNom(); // Access nom via utilisateur
    }

    public String getPrenom() {
        return utilisateur.getPrenom(); // Access prenom via utilisateur
    }
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Getter and Setter for 'specialite'
    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}