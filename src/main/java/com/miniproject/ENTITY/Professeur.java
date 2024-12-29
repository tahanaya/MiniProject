package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Professeur {

    private int id;
    private Utilisateur utilisateur; // Foreign key to Utilisateur (1:1 relationship)
    private String specialite;
}
