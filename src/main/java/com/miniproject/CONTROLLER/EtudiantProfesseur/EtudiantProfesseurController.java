package com.miniproject.CONTROLLER.EtudiantProfesseur;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.ENTITY.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EtudiantProfesseurController {

    // FXML-injected TableView and TableColumns
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
    private TableColumn<Etudiant, String> colDateNaissance;
    @FXML
    private TableColumn<Etudiant, String> colEmail;
    @FXML
    private TableColumn<Etudiant, String> colPromotion;

    // DAO for Etudiant operations
    private final GenericDAO<Etudiant> etudiantDAO = new EtudiantDAOImpl();

    // ObservableList to hold Etudiant data for the TableView
    private final ObservableList<Etudiant> etudiantList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("EtudiantController initialized."); // Debugging statement

        // Initialize table columns with PropertyValueFactory
        setupTableColumns();

        // Load student data from the DAO
        loadEtudiants();

        // Bind the data to the TableView
        etudiantTable.setItems(etudiantList);
    }

    /**
     * Sets up the table columns with appropriate cell value factories.
     */
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPromotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));
    }

    /**
     * Loads all students from the database into the TableView.
     */
    void loadEtudiants() {
        etudiantList.clear(); // Clear existing data
        List<Etudiant> list = etudiantDAO.findAll(); // Fetch data from DAO
        System.out.println("Number of students fetched: " + list.size()); // Debugging statement
        etudiantList.addAll(list); // Add data to ObservableList
    }

    /**
     * Handles viewing the selected student's details.
     */
    @FXML
    private void handleViewStudent() {
        Etudiant selected = etudiantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openViewStudentPopup(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à visualiser.");
        }
    }

    /**
     * Opens the View Student popup window for the selected student.
     *
     * @param etudiant The selected Etudiant to view.
     */
    private void openViewStudentPopup(Etudiant etudiant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant-Professeur/viewEtudiantProfesseur.fxml"));
            Parent root = loader.load();

            // Get the controller of the View Student popup and pass the selected student
            viewEtudiantProfesseurController viewStudentController = loader.getController();
            viewStudentController.setEtudiant(etudiant);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 300));
            popupStage.setTitle("Détails de l'étudiant");
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de visualisation.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
