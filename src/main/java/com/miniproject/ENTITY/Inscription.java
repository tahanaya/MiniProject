package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Inscription {
    private int id;
    private int etudiantId; // Foreign key to Etudiant
    private int moduleId;   // Foreign key to Module
    private String dateInscription; // Format: YYYY-MM-DD
}
