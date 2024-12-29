package com.miniproject.CONTROLLER.PROFESSEUR;


import com.miniproject.DAO.GenericDAO;
import com.miniproject.DAO.ProfesseurDAOImp;
import com.miniproject.ENTITY.Professeur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProfesseurController {
    @FXML
    private TableView<Professeur> professeurTable;
    @FXML private TableColumn<Professeur, Integer> colId;
    @FXML private TableColumn<Professeur, String> colNom;
    @FXML private TableColumn<Professeur, String> colPrenom;
    @FXML private TableColumn<Professeur, String> colSpecialite;

    // DAO for Professeur
    private final GenericDAO<Professeur> professeurDAO = new ProfesseurDAOImp();

    // ObservableList for TableView
    private final ObservableList<Professeur> professeurList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("ProfesseurController initialized."); // Debugging

        // Initialize table columns using PropertyValueFactory
        setupTableColumns();

        // Load data from DAO
        loadProfesseurs();

        // Bind data to TableView
        professeurTable.setItems(professeurList);
    }

    /**
     * Sets up the table columns with appropriate cell value factories.
     */
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));
    }

    /**
     * Loads all students from the database into the TableView.
     */
    void loadProfesseurs() {
        professeurList.clear();
        List<Professeur> list = professeurDAO.findAll();
        System.out.println("Number of students fetched: " + list.size()); // Debugging
        professeurList.addAll(list);
    }

    /**
     * Handles the addition of a new student.
     */
    @FXML
    private void handleAddProfesseur() {
        openAddProfesseurPopup();
    }

    /**
     * Opens the Add Professeur popup window.
     */
    private void openAddProfesseurPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Professeur/add-professeur.fxml"));
            Parent root = loader.load();

            // Get the controller of the Add professeur popup and pass this controller
            AddProfesseurController addProfesseurController = loader.getController();
            addProfesseurController.setProfesseurController(this);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 350));
            popupStage.setTitle("Ajouter un professeur");
            popupStage.initOwner(professeurTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

            // Refresh data after adding a prof
            loadProfesseurs();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    /**
     * Handles updating an existing student.
     */
    @FXML
    private void handleUpdateProfesseur() {
        Professeur selected = professeurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditProfesseurPopup(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un Professeur à modifier.");
        }
    }

    /**
     * Opens the Edit Professeur popup window.
     *
     * @param professeur The selected Professeur to edit.
     */
    private void openEditProfesseurPopup(Professeur professeur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Professeur/edit-professeur.fxml"));
            Parent root = loader.load();

            // Get the controller of the Edit Professeur popup and pass this controller and the selected Professeur
            EditProfesseurController editProfesseurController = loader.getController();
            editProfesseurController.setProfesseurController(this);
            editProfesseurController.setProfesseur(professeur);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 350));
            popupStage.setTitle("Modifier un professeur");
            popupStage.initOwner(professeurTable.getScene().getWindow());

            if (popupStage != null) {
                popupStage.showAndWait();  // Wait for popup to close
            } else {
                System.out.println("popupStage is null.");
            }

            // Refresh data after updating a professeur
            loadProfesseurs();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }

    /**
     * Handles deleting a selected student.
     */
    @FXML
    private void handleDeleteProfesseur() {
        Professeur selected = professeurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Confirm deletion
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer Professeur");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce professeur?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete from database
                professeurDAO.delete(selected.getId());
                System.out.println("Deleted prof with ID: " + selected.getId()); // Debugging

                // Refresh data
                loadProfesseurs();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant supprimé avec succès!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un professeur à supprimer.");
        }
    }

    /**
     * Handles viewing the selected student's details.
     */
    @FXML
    private void handleViewProfesseur() {
        Professeur selected = professeurTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openViewProfesseurPopup(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un professeur à visualiser.");
        }
    }

    /**
     * Opens the View Student popup window.
     *
     * @param professeur The selected Professeur to view.
     */
    private void openViewProfesseurPopup(Professeur professeur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Professeur/view-professeur.fxml"));
            Parent root = loader.load();

            // Get the controller of the View Student popup and pass the selected student
            ViewProfesseurController viewProfesseurController = loader.getController();
            viewProfesseurController.setProfesseur(professeur);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 300));
            popupStage.setTitle("Détails de professeur");
            popupStage.initOwner(professeurTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de visualisation.");
        }
    }

    /**
     * Displays an alert dialog to the user.
     *
     * @param type    The type of alert.
     * @param title   The title of the dialog.
     * @param message The content message.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}