package com.miniproject.DAO;

import com.miniproject.DATABASE.DatabaseConnection;
import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Inscription;

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
