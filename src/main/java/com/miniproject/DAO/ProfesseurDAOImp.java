package com.miniproject.DAO;

import com.miniproject.DATABASE.DatabaseConnection;
import com.miniproject.ENTITY.Professeur;
import com.miniproject.ENTITY.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesseurDAOImp implements GenericDAO<Professeur> {

    private Connection conn;

    public ProfesseurDAOImp() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Professeur findById(int id) {
        Professeur professeur = null;
        try {
            String query = "SELECT * FROM Professeur WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                professeur = new Professeur();
                professeur.setId(resultSet.getInt("id"));
                professeur.setNom(resultSet.getString("nom"));
                professeur.setPrenom(resultSet.getString("prenom"));
                professeur.setSpecialite(resultSet.getString("specialite"));

                // Assuming Utilisateur is tied to Professeur by "utilisateur_id"
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setId(resultSet.getInt("utilisateur_id"));
                professeur.setUtilisateur(utilisateur);
            } else {
                System.out.println("No Professeur found with ID: " + id);  // Debug log
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professeur;
    }


    @Override
    public List<Professeur> findAll() {
        List<Professeur> list = new ArrayList<>();
        String sql = "SELECT p.id, u.nom, u.prenom, p.specialite FROM Professeur p JOIN Utilisateur u ON p.utilisateur_id = u.id";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Professeur prof = mapToProfesseur(rs);
                list.add(prof);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void save(Professeur entity) {
        String sqlUtilisateur = "INSERT INTO utilisateur (username, password, nom, prenom, role) VALUES (?, ?, ?, ?, ?)";
        String sqlProfesseur = "INSERT INTO professeur (utilisateur_id, specialite) VALUES (?, ?)";

        try {
            // First insert the Utilisateur and get the generated ID
            try (PreparedStatement stmt = conn.prepareStatement(sqlUtilisateur, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, entity.getUtilisateur().getUsername());
                stmt.setString(2, entity.getUtilisateur().getPassword());
                stmt.setString(3, entity.getUtilisateur().getNom());
                stmt.setString(4, entity.getUtilisateur().getPrenom());
                stmt.setString(5, entity.getUtilisateur().getRole());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int utilisateurId = generatedKeys.getInt(1);  // Get the generated utilisateur ID
                            entity.getUtilisateur().setId(utilisateurId);  // Set the ID for the Utilisateur
                        }
                    }
                }
            }

            // Now insert the Professeur with the newly generated utilisateur_id
            try (PreparedStatement stmt = conn.prepareStatement(sqlProfesseur)) {
                stmt.setInt(1, entity.getUtilisateur().getId());  // Set the utilisateur's ID
                stmt.setString(2, entity.getSpecialite());

                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Professeur entity) {
        String updateUtilisateurSQL = "UPDATE utilisateur SET nom = ?, prenom = ?, username = ?, password = ?, role = ? WHERE id = ?";
        String updateProfesseurSQL = "UPDATE professeur SET specialite = ? WHERE utilisateur_id = ?";

        try {
            // Start transaction
            conn.setAutoCommit(false);

            // 1. Update the 'utilisateur' table first
            try (PreparedStatement stmt = conn.prepareStatement(updateUtilisateurSQL)) {
                stmt.setString(1, entity.getNom());
                stmt.setString(2, entity.getPrenom());
                stmt.setString(3, entity.getUtilisateur().getUsername());  // Username (if editable)
                stmt.setString(4, entity.getUtilisateur().getPassword());  // Password (if editable)
                stmt.setString(5, entity.getUtilisateur().getRole());
                stmt.setInt(6, entity.getUtilisateur().getId());  // 'Utilisateur' ID

                int utilisateurRowsAffected = stmt.executeUpdate();
                if (utilisateurRowsAffected > 0) {
                    System.out.println("Successfully updated 'utilisateur' with ID: " + entity.getUtilisateur().getId());
                } else {
                    System.out.println("No 'utilisateur' found with ID: " + entity.getUtilisateur().getId());
                }
            }

            // 2. Update the 'professeur' table next
            try (PreparedStatement stmt = conn.prepareStatement(updateProfesseurSQL)) {
                stmt.setString(1, entity.getSpecialite());  // Speciality
                stmt.setInt(2, entity.getUtilisateur().getId());  // Linking with 'utilisateur_id'

                int professeurRowsAffected = stmt.executeUpdate();
                if (professeurRowsAffected > 0) {
                    System.out.println("Successfully updated 'professeur' with utilisateur_id: " + entity.getUtilisateur().getId());
                } else {
                    System.out.println("No 'professeur' found with utilisateur_id: " + entity.getUtilisateur().getId());
                }
            }

            // Commit the transaction if both updates are successful
            conn.commit();

        } catch (SQLException e) {
            // Handle exception and rollback transaction if something goes wrong
            System.err.println("Error during update operation: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back successfully.");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
        } finally {
            // Reset auto-commit to true to avoid issues later
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
    public void delete(int id) {
        String deleteProfesseurQuery = "DELETE FROM professeur WHERE id = ?";
        try {
            // Start transaction
            conn.setAutoCommit(false);

            try (PreparedStatement pstmtProfesseur = conn.prepareStatement(deleteProfesseurQuery)) {
                // Delete the professeur
                pstmtProfesseur.setInt(1, id);
                pstmtProfesseur.executeUpdate();
            }

            // Commit transaction
            conn.commit();
        } catch (SQLException e) {
            // Handle exception and rollback transaction
            System.err.println("Error during delete operation: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Transaction rolled back successfully.");
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

    /**
     * Helper method to map a ResultSet row to an Professeur object
     */
    private Professeur mapToProfesseur(ResultSet rs) throws SQLException {
        // Log the column names in the result set
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            System.out.println("Column " + i + ": " + metaData.getColumnName(i));
        }

        Professeur p = new Professeur();
        p.setId(rs.getInt("id"));

        // Assuming 'nom' and 'prenom' are in the Utilisateur table
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        p.setUtilisateur(utilisateur);

        p.setSpecialite(rs.getString("specialite"));

        return p;
    }



}


