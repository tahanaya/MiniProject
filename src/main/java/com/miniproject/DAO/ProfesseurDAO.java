package com.miniproject.DAO;

import com.miniproject.ENTITY.Professeur;
import com.miniproject.DATABASE.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProfesseurDAO implements GenericDAO<Professeur> {

    private Connection connection;

    public ProfesseurDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Professeur findById(int id) {
        Professeur professeur = null;
        String query = "SELECT * FROM professeurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                professeur = new Professeur();
                professeur.setId(rs.getInt("id"));
                professeur.setNom(rs.getString("nom"));
                professeur.setPrenom(rs.getString("prenom"));
                professeur.setSpecialite(rs.getString("specialite"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professeur;
    }

    @Override
    public List<Professeur> findAll() {
        List<Professeur> professeurs = new ArrayList<>();
        String query = "SELECT * FROM professeurs";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Professeur professeur = new Professeur();
                professeur.setId(rs.getInt("id"));
                professeur.setNom(rs.getString("nom"));
                professeur.setPrenom(rs.getString("prenom"));
                professeur.setSpecialite(rs.getString("specialite"));
                professeurs.add(professeur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return professeurs;
    }

    @Override
    public void save(Professeur professeur) {
        String query = "INSERT INTO professeurs (nom, prenom, specialite) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, professeur.getNom());
            stmt.setString(2, professeur.getPrenom());
            stmt.setString(3, professeur.getSpecialite());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Professeur professeur) {
        String query = "UPDATE professeurs SET nom = ?, prenom = ?, specialite = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, professeur.getNom());
            stmt.setString(2, professeur.getPrenom());
            stmt.setString(3, professeur.getSpecialite());
            stmt.setInt(4, professeur.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM professeurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
