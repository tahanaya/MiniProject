package com.miniproject.CONTROLLER.ETUDIANT;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.ENTITY.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing Etudiant (Student) records.
 * Provides functionalities to view, add, edit, delete, and export student data.
 */
public class EtudiantController {

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
     * Handles the addition of a new student.
     */
    @FXML
    private void handleAddStudent() {
        openAddStudentPopup();
    }

    /**
     * Handles updating an existing student.
     */
    @FXML
    private void handleUpdateStudent() {
        Etudiant selected = etudiantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditStudentPopup(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à modifier.");
        }
    }

    /**
     * Handles deleting a selected student.
     */
    @FXML
    private void handleDeleteStudent() {
        Etudiant selected = etudiantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Confirm deletion
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer Étudiant");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet étudiant?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete from database
                etudiantDAO.delete(selected.getId());
                System.out.println("Deleted student with ID: " + selected.getId()); // Debugging statement

                // Refresh data
                loadEtudiants();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant supprimé avec succès!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à supprimer.");
        }
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
     * Handles exporting the list of students to a CSV file.
     */
    @FXML
    private void handleExportStudents() {
        // Open a FileChooser to select the destination file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les Étudiants en CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show save dialog
        File file = fileChooser.showSaveDialog(etudiantTable.getScene().getWindow());
        if (file != null) {
            exportToCSV(file);
        }
    }

    /**
     * Exports the list of students to a CSV file using standard Java I/O.
     *
     * @param file The destination file.
     */
    private void exportToCSV(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write CSV header
            writer.write("ID,Matricule,Nom,Prenom,DateNaissance,Email,Promotion");
            writer.newLine();

            // Write student data
            for (Etudiant etudiant : etudiantList) {
                // Escape commas, quotes, and newlines in data fields
                String id = String.valueOf(etudiant.getId());
                String matricule = escapeSpecialCharacters(etudiant.getMatricule());
                String nom = escapeSpecialCharacters(etudiant.getNom());
                String prenom = escapeSpecialCharacters(etudiant.getPrenom());
                String dateNaissance = escapeSpecialCharacters(etudiant.getDateNaissance());
                String email = escapeSpecialCharacters(etudiant.getEmail());
                String promotion = escapeSpecialCharacters(etudiant.getPromotion());

                String line = String.format("%s,%s,%s,%s,%s,%s,%s",
                        id, matricule, nom, prenom, dateNaissance, email, promotion);
                writer.write(line);
                writer.newLine();
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les étudiants ont été exportés en CSV avec succès!");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'exportation en CSV.");
        }
    }

    /**
     * Escapes special characters in CSV fields.
     * Encloses the field in quotes if it contains commas, quotes, or newlines.
     * Doubles any existing quotes within the field.
     *
     * @param data The data string to escape.
     * @return The escaped data string.
     */
    private String escapeSpecialCharacters(String data) {
        String escapedData = data;
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    /**
     * Opens the Add Student popup window.
     */
    private void openAddStudentPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/add-student.fxml"));
            Parent root = loader.load();

            // Get the controller of the Add Student popup and pass this controller
            AddStudentController addStudentController = loader.getController();
            addStudentController.setEtudiantController(this);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 350));
            popupStage.setTitle("Ajouter un étudiant");
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

            // Refresh data after adding a student
            loadEtudiants();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    /**
     * Opens the Edit Student popup window for the selected student.
     *
     * @param etudiant The selected Etudiant to edit.
     */
    private void openEditStudentPopup(Etudiant etudiant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/edit-student.fxml"));
            Parent root = loader.load();

            // Get the controller of the Edit Student popup and pass this controller and the selected student
            EditStudentController editStudentController = loader.getController();
            editStudentController.setEtudiantController(this);
            editStudentController.setEtudiant(etudiant);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 350));
            popupStage.setTitle("Modifier un étudiant");
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

            // Refresh data after updating a student
            loadEtudiants();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }

    /**
     * Opens the View Student popup window for the selected student.
     *
     * @param etudiant The selected Etudiant to view.
     */
    private void openViewStudentPopup(Etudiant etudiant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/view-student.fxml"));
            Parent root = loader.load();

            // Get the controller of the View Student popup and pass the selected student
            ViewStudentController viewStudentController = loader.getController();
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
