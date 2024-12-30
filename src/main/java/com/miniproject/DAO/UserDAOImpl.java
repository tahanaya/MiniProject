package com.miniproject.DAO;

import com.miniproject.ENTITY.Utilisateur;
import com.miniproject.DATABASE.DatabaseConnection;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    private Connection connection;

    public UserDAOImpl() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Utilisateur getUserByUsername(String username) {
        String query = "SELECT * FROM utilisateur WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur user = new Utilisateur();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addUser(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateur (username, password, nom, prenom, role) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, utilisateur.getUsername());
            stmt.setString(2, utilisateur.getPassword());
            stmt.setString(3, utilisateur.getNom());
            stmt.setString(4, utilisateur.getPrenom());
            stmt.setString(5, utilisateur.getRole()); // Set a role if needed, like "teacher" or null

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int utilisateurId = generatedKeys.getInt(1);
                        utilisateur.setId(utilisateurId);  // Set the generated ID on the Utilisateur object
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE utilisateur SET username = ?, password = ?, role = ?, nom = ?, prenom = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getUsername());
            stmt.setString(2, utilisateur.getPassword());
            stmt.setString(3, utilisateur.getRole());
            stmt.setString(4, utilisateur.getNom());
            stmt.setString(5, utilisateur.getPrenom());
            stmt.setInt(6, utilisateur.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        }
    }
}