package com.miniproject.DAO;

import com.miniproject.DATABASE.DatabaseConnection;
import com.miniproject.ENTITY.Professeur;
import com.miniproject.ENTITY.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesseurDAOImpl implements ProfesseurDAO {
    private final Connection conn;

    public ProfesseurDAOImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void save(Professeur entity) {
        String sqlUtilisateur = "INSERT INTO utilisateur (username, password, nom, prenom, role) VALUES (?, ?, ?, ?, ?)";
        String sqlProfesseur = "INSERT INTO professeur (utilisateur_id, specialite) VALUES (?, ?)";

        try {
            // Start transaction
            conn.setAutoCommit(false);

            // 1. Insert into 'utilisateur' table and retrieve generated ID
            try (PreparedStatement stmt = conn.prepareStatement(sqlUtilisateur, Statement.RETURN_GENERATED_KEYS)) {
                Utilisateur utilisateur = entity.getUtilisateur();

                stmt.setString(1, utilisateur.getUsername());
                stmt.setString(2, utilisateur.getPassword());
                stmt.setString(3, utilisateur.getNom());
                stmt.setString(4, utilisateur.getPrenom());
                stmt.setString(5, utilisateur.getRole());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating utilisateur failed, no rows affected.");
                }

                // Retrieve the generated 'utilisateur_id'
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int utilisateurId = generatedKeys.getInt(1);
                        utilisateur.setId(utilisateurId); // Set the ID for Utilisateur
                        System.out.println("Inserted Utilisateur with ID: " + utilisateurId);
                    } else {
                        throw new SQLException("Creating utilisateur failed, no ID obtained.");
                    }
                }
            }

            // 2. Insert into 'professeur' table using the retrieved 'utilisateur_id'
            try (PreparedStatement stmt = conn.prepareStatement(sqlProfesseur, Statement.RETURN_GENERATED_KEYS)) {
                Utilisateur utilisateur = entity.getUtilisateur();

                stmt.setInt(1, utilisateur.getId()); // 'utilisateur_id' from Utilisateur
                stmt.setString(2, entity.getSpecialite());

                int professeurRowsAffected = stmt.executeUpdate();
                if (professeurRowsAffected == 0) {
                    throw new SQLException("Creating professeur failed, no rows affected.");
                }

                // Retrieve the generated 'professeur_id'
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int professeurId = generatedKeys.getInt(1);
                        entity.setId(professeurId); // Set the ID for Professeur
                        System.out.println("Inserted Professeur with ID: " + professeurId);
                    } else {
                        throw new SQLException("Creating professeur failed, no ID obtained.");
                    }
                }
            }

            // Commit transaction
            conn.commit();
            System.out.println("Transaction committed successfully.");

        } catch (SQLException e) {
            System.err.println("Error during save operation: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to errors.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
        } finally {
            // Reset auto-commit to true
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Error resetting auto-commit: " + ex.getMessage());
            }
        }
    }

    @Override
    public void update(Professeur professeur)throws SQLException {
        String sql = "UPDATE professeur SET specialite = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, professeur.getSpecialite());
            stmt.setInt(2, professeur.getId());
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating professeur failed, no rows affected.");
            }
        }
    }

    @Override
    public Professeur findById(int id) {
        String sql = "SELECT p.id as professeur_id, u.id as utilisateur_id, u.username, u.password, u.nom, u.prenom, u.role, p.specialite " +
                "FROM professeur p " +
                "JOIN utilisateur u ON p.utilisateur_id = u.id " +
                "WHERE p.id = ?";

        Professeur professeur = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("utilisateur_id"));
                    utilisateur.setUsername(rs.getString("username"));
                    utilisateur.setPassword(rs.getString("password"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setRole(rs.getString("role"));

                    professeur = new Professeur();
                    professeur.setId(rs.getInt("professeur_id"));
                    professeur.setUtilisateur(utilisateur);
                    professeur.setSpecialite(rs.getString("specialite"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during findById operation: " + e.getMessage());
        }

        return professeur;
    }

    @Override
    public List<Professeur> findAll() {
        String sql = "SELECT p.id as professeur_id, u.id as utilisateur_id, u.username, u.password, u.nom, u.prenom, u.role, p.specialite " +
                "FROM professeur p " +
                "JOIN utilisateur u ON p.utilisateur_id = u.id";

        List<Professeur> professeurs = new ArrayList<>();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(rs.getInt("utilisateur_id"));
                utilisateur.setUsername(rs.getString("username"));
                utilisateur.setPassword(rs.getString("password"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setRole(rs.getString("role"));

                Professeur professeur = new Professeur();
                professeur.setId(rs.getInt("professeur_id"));
                professeur.setUtilisateur(utilisateur);
                professeur.setSpecialite(rs.getString("specialite"));

                professeurs.add(professeur);
            }

        } catch (SQLException e) {
            System.err.println("Error during findAll operation: " + e.getMessage());
        }

        return professeurs;
    }

    @Override
    public void delete(int id) {
        String deleteProfesseurSQL = "DELETE FROM professeur WHERE id = ?";
        String deleteUtilisateurSQL = "DELETE FROM utilisateur WHERE id = (SELECT utilisateur_id FROM professeur WHERE id = ?)";

        try {
            // Start transaction
            conn.setAutoCommit(false);

            // 1. Delete from 'professeur' table
            try (PreparedStatement stmt = conn.prepareStatement(deleteProfesseurSQL)) {
                stmt.setInt(1, id);

                int professeurRowsAffected = stmt.executeUpdate();
                if (professeurRowsAffected > 0) {
                    System.out.println("Successfully deleted 'professeur' with ID: " + id);
                } else {
                    System.out.println("No 'professeur' found with ID: " + id);
                }
            }

            // 2. Delete from 'utilisateur' table
            try (PreparedStatement stmt = conn.prepareStatement(deleteUtilisateurSQL)) {
                stmt.setInt(1, id);

                int utilisateurRowsAffected = stmt.executeUpdate();
                if (utilisateurRowsAffected > 0) {
                    System.out.println("Successfully deleted 'utilisateur' linked to Professeur ID: " + id);
                } else {
                    System.out.println("No 'utilisateur' found linked to Professeur ID: " + id);
                }
            }

            // Commit transaction
            conn.commit();
            System.out.println("Transaction committed successfully.");

        } catch (SQLException e) {
            System.err.println("Error during delete operation: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back due to errors.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
        } finally {
            // Reset auto-commit to true
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                System.err.println("Error resetting auto-commit: " + ex.getMessage());
            }
        }
    }

    @Override
    public Professeur findByUsername(String username) {
        String sql = "SELECT p.id as professeur_id, u.id as utilisateur_id, u.username, u.password, u.nom, u.prenom, u.role, p.specialite " +
                "FROM professeur p " +
                "JOIN utilisateur u ON p.utilisateur_id = u.id " +
                "WHERE u.username = ?";

        Professeur professeur = null;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setId(rs.getInt("utilisateur_id"));
                    utilisateur.setUsername(rs.getString("username"));
                    utilisateur.setPassword(rs.getString("password"));
                    utilisateur.setNom(rs.getString("nom"));
                    utilisateur.setPrenom(rs.getString("prenom"));
                    utilisateur.setRole(rs.getString("role"));

                    professeur = new Professeur();
                    professeur.setId(rs.getInt("professeur_id"));
                    professeur.setUtilisateur(utilisateur);
                    professeur.setSpecialite(rs.getString("specialite"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error during findByUsername operation: " + e.getMessage());
        }

        return professeur;
    }
}
