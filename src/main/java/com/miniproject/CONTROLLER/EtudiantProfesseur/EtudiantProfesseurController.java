package com.miniproject.CONTROLLER.EtudiantProfesseur;

import com.miniproject.ENTITY.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class EtudiantProfesseurController {

    @FXML
    private TableView<Etudiant> etudiantTable;
    @FXML
    private TableColumn<Etudiant, Integer> colId;
    @FXML
    private TableColumn<Etudiant, String> colMatricule;
    @FXML
    private TableColumn<Etudiant, String> colNom;
    @FXML
    private TableColumn<Etudiant, String> colPrenom;
    @FXML
    private TableColumn<Etudiant, LocalDate> colDateNaissance;
    @FXML
    private TableColumn<Etudiant, String> colEmail;
    @FXML
    private TableColumn<Etudiant, String> colPromotion;

    private Connection connection; // Connection déjà configurée

    // Constructeur ou initialisation de la connexion
    public EtudiantProfesseurController(Connection connection) {
        this.connection = connection; // Injectez votre connexion ici
    }

    @FXML
    private void initialize() {
        // Lier les colonnes aux propriétés de l'objet Etudiant
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPromotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));

        // Charger les données dans la TableView
        etudiantTable.setItems(getEtudiants());
    }

    private ObservableList<Etudiant> getEtudiants() {
        ObservableList<Etudiant> etudiantList = FXCollections.observableArrayList();
        String query = "SELECT id, matricule, nom, prenom, dateNaissance, email, promotion FROM etudiants";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                // Ajouter les données dans la liste
                Etudiant etudiant = new Etudiant(
                        rs.getInt("id"),
                        rs.getString("matricule"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("dateNaissance"),
                        rs.getString("email"),
                        rs.getString("promotion")
                );
                etudiantList.add(etudiant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Vous pouvez afficher une alerte à l'utilisateur en cas d'erreur
        }

        return etudiantList;
    }


    @FXML
    private void handleViewStudent() {
        Etudiant selectedEtudiant = etudiantTable.getSelectionModel().getSelectedItem();
        if (selectedEtudiant != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/VIEW/ViewEtudiantProfesseur.fxml"));
                Parent root = loader.load();

                ViewEtudiantProfesseurController controller = loader.getController();
                controller.setEtudiant(selectedEtudiant);

                Stage stage = new Stage();
                stage.setTitle("Détails de l'Étudiant");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Aucun étudiant sélectionné");
        }
    }
}
