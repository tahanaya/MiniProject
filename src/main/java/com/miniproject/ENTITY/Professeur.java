package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Professeur {
    private int id; // Professeur's primary key
    private Utilisateur utilisateur; // Associated Utilisateur (1:1 relationship)
    private String specialite; // Specialization

    public String getNom() {
        return utilisateur != null ? utilisateur.getNom() : "";
    }

    public String getPrenom() {
        return utilisateur != null ? utilisateur.getPrenom() : "";
    }

    public void setNom(String text) {
        this.utilisateur.setNom(text);
    }
    public void setPrenom(String text) {
        this.utilisateur.setPrenom(text);
    }
}
