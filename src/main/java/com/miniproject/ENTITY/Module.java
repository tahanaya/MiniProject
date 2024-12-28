package com.miniproject.ENTITY;

import lombok.Data;

@Data
public class Module {
    private int id;
    private String nomModule;
    private String codeModule;
    private int professeurId; // Foreign key to Professeur
}
