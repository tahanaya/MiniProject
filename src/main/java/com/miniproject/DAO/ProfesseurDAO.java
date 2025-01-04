package com.miniproject.DAO;

import com.miniproject.ENTITY.Professeur;

import java.util.List;

public interface ProfesseurDAO extends GenericDAO<Professeur> {
    // Additional methods can be defined here if needed
    Professeur findByUsername(String username);
}

