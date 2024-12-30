package com.miniproject.DAO;

import com.miniproject.ENTITY.Utilisateur;

import java.sql.SQLException;

public interface UserDAO {
    Utilisateur getUserByUsername(String username);
    void addUser(Utilisateur user);
    void updateUser(Utilisateur user)throws SQLException;
}
