package com.miniproject.ENTITY;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Inscription {

    private int id;
    private Etudiant etudiant; // Foreign key to Etudiant (many-to-one relationship)
    private Module module; // Foreign key to Module (many-to-one relationship)
    private LocalDate dateInscription; // Date of inscription
}
