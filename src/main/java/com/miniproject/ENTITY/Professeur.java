package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Professeur {
    private int id;
    private Utilisateur utilisateur;
    private String specialite;

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
