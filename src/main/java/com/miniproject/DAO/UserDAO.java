package com.miniproject.DAO;

import com.miniproject.ENTITY.Utilisateur;

public interface UserDAO {
    Utilisateur getUserByUsername(String username);
    void addUser(Utilisateur user);
}
