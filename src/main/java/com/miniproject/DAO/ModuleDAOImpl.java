package com.miniproject.DAO;

import com.miniproject.DATABASE.DatabaseConnection;
import com.miniproject.ENTITY.Module;
import com.miniproject.ENTITY.Professeur;
import com.miniproject.ENTITY.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleDAOImpl implements GenericDAO<Module> {

    // Database connection
    private final Connection conn = DatabaseConnection.getInstance().getConnection();

    // Find module by ID
    @Override
    public Module findById(int id) {
        String sql = """
                SELECT m.id AS module_id, m.nomModule, m.codeModule, p.id AS professeur_id, 
                       u.id AS utilisateur_id, u.nom, u.prenom, u.username, u.password, u.role, p.specialite
                FROM module m
                JOIN professeur p ON m.professeur_id = p.id
                JOIN utilisateur u ON p.utilisateur_id = u.id
                WHERE m.id = ?""";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToModule(rs); // Map result to Module
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding module by ID: " + e.getMessage());
        }
        return null;
    }

    // Retrieve all modules
    @Override
    public List<Module> findAll() {
        List<Module> modules = new ArrayList<>();
        String sql = """
                SELECT m.id AS module_id, m.nomModule, m.codeModule, p.id AS professeur_id, 
                       u.id AS utilisateur_id, u.nom, u.prenom, u.username, u.password, u.role, p.specialite
                FROM module m
                JOIN professeur p ON m.professeur_id = p.id
                JOIN utilisateur u ON p.utilisateur_id = u.id
                ORDER BY m.id ASC""";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                modules.add(mapToModule(rs)); // Map each row to a Module
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all modules: " + e.getMessage());
        }
        return modules;
    }

    // Save a new module
    @Override
    public void save(Module module) {
        String sql = "INSERT INTO module (nomModule, codeModule, professeur_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, module.getNomModule());
            stmt.setString(2, module.getCodeModule());
            stmt.setInt(3, module.getProfesseur().getId());
            stmt.executeUpdate();

            // Get auto-generated ID
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    module.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error saving module: " + e.getMessage());
        }
    }

    // Update an existing module
    @Override
    public void update(Module module) {
        String sql = "UPDATE module SET nomModule = ?, codeModule = ?, professeur_id = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, module.getNomModule());
            stmt.setString(2, module.getCodeModule());
            stmt.setInt(3, module.getProfesseur().getId());
            stmt.setInt(4, module.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating module: " + e.getMessage());
        }
    }

    // Delete a module
    @Override
    public void delete(int id) {
        String deleteInscription = "DELETE FROM inscription WHERE module_id = ?";
        String deleteModule = "DELETE FROM module WHERE id = ?";
        try {
            conn.setAutoCommit(false);

            // Delete related inscriptions
            try (PreparedStatement stmtInscription = conn.prepareStatement(deleteInscription)) {
                stmtInscription.setInt(1, id);
                stmtInscription.executeUpdate();
            }

            // Delete module
            try (PreparedStatement stmtModule = conn.prepareStatement(deleteModule)) {
                stmtModule.setInt(1, id);
                stmtModule.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            System.err.println("Error deleting module: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Error resetting auto-commit: " + e.getMessage());
            }
        }
    }

    // Map a ResultSet row to a Module object
    private Module mapToModule(ResultSet rs) throws SQLException {
        Module module = new Module();
        module.setId(rs.getInt("module_id"));
        module.setNomModule(rs.getString("nomModule"));
        module.setCodeModule(rs.getString("codeModule"));
        module.setDeadline(rs.getDate("deadline").toLocalDate()); // Map the deadline correctly


        Professeur professeur = new Professeur();
        professeur.setId(rs.getInt("professeur_id"));

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getInt("utilisateur_id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setUsername(rs.getString("username"));
        utilisateur.setPassword(rs.getString("password"));
        utilisateur.setRole(rs.getString("role"));

        professeur.setUtilisateur(utilisateur);
        professeur.setSpecialite(rs.getString("specialite"));

        module.setProfesseur(professeur);
        System.out.println("Module: " + module.getNomModule() + ", Professor: " + module.getProfessorFullName());

        return module;
    }
    //Méthode qui recalcule les IDs
    public void reorganizeIds() {
        String resetSequence = "ALTER TABLE module AUTO_INCREMENT = 1";
        String updateIds = "SET @count = 0; " +
                "UPDATE module SET id = (@count := @count + 1);";
        try (Statement stmt = conn.createStatement()) {
            // Reset the auto-increment counter
            stmt.execute(resetSequence);
            // Recalculate IDs
            stmt.execute(updateIds);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la réorganisation des IDs : " + e.getMessage());
        }
    }

    public int countModules() throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM module";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }



    //the most followed modules
    public Map<String, Integer> getMostFollowedModules() {
        Map<String, Integer> result = new HashMap<>();
        String sql = """
        SELECT m.nomModule, COUNT(i.module_id) AS count
        FROM module m
        JOIN inscription i ON m.id = i.module_id
        GROUP BY m.id, m.nomModule
        ORDER BY count DESC
        LIMIT 5;  
    """;
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.put(rs.getString("nomModule"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Module findModuleByName(String moduleName) {
        String sql = """
        SELECT m.id AS module_id, m.nomModule, m.codeModule, p.id AS professeur_id, 
               u.id AS utilisateur_id, u.nom, u.prenom, u.username, u.password, u.role, p.specialite, m.deadline
        FROM module m
        JOIN professeur p ON m.professeur_id = p.id
        JOIN utilisateur u ON p.utilisateur_id = u.id
        WHERE m.nomModule = ?
    """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, moduleName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapToModule(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding module by name: " + e.getMessage());
        }
        return null;
    }


    //modules with deadlines approaching
    public List<Module> getModulesWithUpcomingDeadlines(int days) {
        String sql = """
        SELECT * 
        FROM module
        WHERE deadline BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL ? DAY
    """;

        List<Module> modules = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, days);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Module module = mapToModule(rs);
                    modules.add(module);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }



}
