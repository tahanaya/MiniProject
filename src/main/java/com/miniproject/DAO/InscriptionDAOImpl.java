package com.miniproject.DAO;

import com.miniproject.DATABASE.DatabaseConnection;
import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Inscription;
import com.miniproject.ENTITY.Professeur;
import com.miniproject.ENTITY.Utilisateur;
import com.miniproject.ENTITY.Module;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDAOImpl implements GenericDAO<Inscription> {
    private final Connection conn = DatabaseConnection.getInstance().getConnection();
    @Override
    public Inscription findById(int id) {
        return null;
    }

    @Override
    public List<Inscription> findAll() {
        return List.of();
    }

    @Override
    public void save(Inscription entity) {

    }

    @Override
    public void update(Inscription entity) {

    }

    @Override
    public void delete(int id) {

    }
    //check if a student is already associated with a module
    public boolean isStudentInModule(int moduleId, int studentId) {
        String sql = "SELECT COUNT(*) FROM inscription WHERE etudiant_id = ? AND module_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Returns true if the count is greater than 0
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'association : " + e.getMessage());
        }
        return false;
    }

    // Add a student to a module
    public void addStudentToModule(int moduleId, int studentId) {
        String sql = "INSERT INTO inscription (etudiant_id, module_id, dateInscription) VALUES (?, ?, NOW())";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, moduleId);
            stmt.executeUpdate();
            System.out.println("Étudiant ajouté au module avec succès !");
        } catch (SQLException e) {
            System.err.println("Error adding student to module: " + e.getMessage());
        }
    }
    // Get students associated with a module

    public List<Etudiant> getStudentsByModuleId(int moduleId) {
        List<Etudiant> students = new ArrayList<>();
        String sql = """
                SELECT e.*
                FROM etudiant e
                INNER JOIN inscription i ON e.id = i.etudiant_id
                WHERE i.module_id = ?""";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    students.add(mapToEtudiant(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving students by module ID: " + e.getMessage());
        }
        return students;
    }
    public List<Module> getModulesByStudentId(int studentId) {
        List<Module> modules = new ArrayList<>();
        String sql = """
            SELECT m.id, m.nomModule, m.codeModule, 
                   p.id AS prof_id, u.nom AS prof_nom, u.prenom AS prof_prenom
            FROM module m
            JOIN inscription i ON m.id = i.module_id
            JOIN professeur p ON m.professeur_id = p.id
            JOIN utilisateur u ON p.utilisateur_id = u.id
            WHERE i.etudiant_id = ?""";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Module module = new Module();
                    module.setId(rs.getInt("id"));
                    module.setNomModule(rs.getString("nomModule"));
                    module.setCodeModule(rs.getString("codeModule"));

                    Professeur prof = new Professeur();
                    prof.setId(rs.getInt("prof_id"));
                    Utilisateur utilisateur = new Utilisateur();
                    utilisateur.setNom(rs.getString("prof_nom"));
                    utilisateur.setPrenom(rs.getString("prof_prenom"));
                    prof.setUtilisateur(utilisateur);
                    module.setProfesseur(prof);

                    modules.add(module);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving modules by student ID: " + e.getMessage());
        }
        return modules;
    }

    private Etudiant mapToEtudiant(ResultSet rs) throws SQLException {
        Etudiant student = new Etudiant();
        student.setId(rs.getInt("id")); // This is the primary key from the "etudiant" table
        student.setMatricule(rs.getString("matricule"));
        student.setNom(rs.getString("nom"));
        student.setPrenom(rs.getString("prenom"));
        student.setDateNaissance(rs.getDate("dateNaissance").toString());
        student.setEmail(rs.getString("email"));
        student.setPromotion(rs.getString("promotion"));
        return student;
    }}
