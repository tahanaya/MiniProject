package com.miniproject.DAO;

import com.miniproject.DAO.GenericDAO;
import com.miniproject.DATABASE.DatabaseConnection;
import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Professeur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfesseurDAO  implements GenericDAO<Professeur> {


    private Connection conn;

    public ProfesseurDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Professeur findById(int id) {
        return null;
    }

    @Override
    public List<Professeur> findAll() {
        return List.of();
    }

    @Override
    public void save(Professeur entity) {

    }

    @Override
    public void update(Professeur entity) {

    }

    @Override
    public void delete(int id) {

    }

    public List<Etudiant> getStudentsEnrolled(int professorId) throws SQLException {
        List<Etudiant> enrolledStudents = new ArrayList<>();

        String query = """
             
                SELECT DISTINCT\s
                 e.id,\s
                 e.matricule,\s
                 e.nom,\s
                 e.prenom,\s
                 e.dateNaissance,\s
                 e.email,\s
                 e.promotion
             FROM\s
                 etudiants e
             JOIN\s
                 inscriptions i ON e.id = i.etudiant_id
             JOIN\s
                 modules m ON i.module_id = m.id
             WHERE\s
                 m.professeur_id = ?
             ORDER BY\s
                 e.nom, e.prenom;
             
                """;

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, professorId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String matricule = rs.getString("matricule");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String dateNaissance = rs.getDate("dateNaissance").toString(); // Convert Date to String
                    String email = rs.getString("email");
                    String promotion = rs.getString("promotion");

                    Etudiant etudiant = new Etudiant(id, matricule, nom, prenom, dateNaissance, email, promotion);
                    enrolledStudents.add(etudiant);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow after logging
        }

        return enrolledStudents;
    }

}