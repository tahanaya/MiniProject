package com.miniproject.DAO;

import com.miniproject.DATABASE.DatabaseConnection;
import com.miniproject.ENTITY.Etudiant;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  EtudiantDAOImpl implements EtudiantDAO{

    private Connection conn;

    public EtudiantDAOImpl() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Etudiant findById(int id) {
        String sql = "SELECT * FROM etudiant WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapToEtudiant(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Etudiant> findAll() {
        List<Etudiant> list = new ArrayList<>();
        String sql = "SELECT * FROM etudiant ORDER BY id ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Etudiant etu = mapToEtudiant(rs);
                list.add(etu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void save(Etudiant entity) {
        // If 'id' is auto-generated (SERIAL), we typically ignore it here
        String sql = "INSERT INTO etudiant (matricule, nom, prenom, dateNaissance, email, promotion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getMatricule());
            stmt.setString(2, entity.getNom());
            stmt.setString(3, entity.getPrenom());
            stmt.setDate(4, Date.valueOf(entity.getDateNaissance())); // 'YYYY-MM-DD' → Date.valueOf()
            stmt.setString(5, entity.getEmail());
            stmt.setString(6, entity.getPromotion());

            stmt.executeUpdate();
            // Optionally get auto-generated ID
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                entity.setId(keys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Etudiant entity) {
        String sql = "UPDATE etudiant SET "
                + "matricule=?, nom=?, prenom=?, dateNaissance=?, email=?, promotion=? "
                + "WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, entity.getMatricule());
            stmt.setString(2, entity.getNom());
            stmt.setString(3, entity.getPrenom());
            stmt.setDate(4, Date.valueOf(entity.getDateNaissance()));
            stmt.setString(5, entity.getEmail());
            stmt.setString(6, entity.getPromotion());
            stmt.setInt(7, entity.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String deleteInscriptions = "DELETE FROM inscription WHERE etudiant_id = ?";
        String deleteEtudiant = "DELETE FROM etudiant WHERE id = ?";
        try (PreparedStatement pstmtInscriptions = conn.prepareStatement(deleteInscriptions);
             PreparedStatement pstmtEtudiant = conn.prepareStatement(deleteEtudiant)) {

            // Start transaction
            conn.setAutoCommit(false);

            // Delete related inscriptions
            pstmtInscriptions.setInt(1, id);
            pstmtInscriptions.executeUpdate();

            // Delete the etudiant
            pstmtEtudiant.setInt(1, id);
            pstmtEtudiant.executeUpdate();

            // Commit transaction
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Helper method to map a ResultSet row to an Etudiant object
     */
    public Etudiant mapToEtudiant(ResultSet rs) throws SQLException {
        Etudiant e = new Etudiant();
        e.setId(rs.getInt("id"));
        e.setMatricule(rs.getString("matricule"));
        e.setNom(rs.getString("nom"));
        e.setPrenom(rs.getString("prenom"));
        // Convert SQL date to String "YYYY-MM-DD"
        Date dbDate = rs.getDate("dateNaissance");
        e.setDateNaissance(dbDate != null ? dbDate.toString() : null);
        e.setEmail(rs.getString("email"));
        e.setPromotion(rs.getString("promotion"));
        return e;
    }

    //Calculer le nombre des étudiants
    public int countStudents() {
        String sql = "SELECT COUNT(*) AS count FROM etudiant";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Etudiant> getStudentsWithoutModules() {
        String sql = """
        SELECT e.*
        FROM etudiant e
        WHERE e.id NOT IN (
            SELECT etudiant_id FROM inscription
        )
    """;

        List<Etudiant> students = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                students.add(mapToEtudiant(rs));
                System.out.println("Students without modules: " + students.size());
                students.forEach(student -> System.out.println(student));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;

    }


}
