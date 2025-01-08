package com.miniproject.ENTITY;

import lombok.Data;
import java.time.LocalDate;

@Data
public class Inscription {

    private int id;
    private Etudiant etudiant;
    private Module module;
    private LocalDate dateInscription;
}
