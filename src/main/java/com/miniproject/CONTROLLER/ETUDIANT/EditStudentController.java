package com.miniproject.CONTROLLER.ETUDIANT;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.ENTITY.Etudiant;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class EditStudentController {

    @FXML private TextField matriculeField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField dateNaissanceField;
    @FXML private TextField emailField;
    @FXML private TextField promotionField;

    // Reference to EtudiantController to refresh data
    private EtudiantController etudiantController;

    // The student to edit
    private Etudiant etudiant;

    // DAO for Etudiant
    private final GenericDAO<Etudiant> etudiantDAO = new EtudiantDAOImpl();

    /**
     * Sets the EtudiantController reference.
     *
     * @param controller The EtudiantController instance.
     */
    public void setEtudiantController(EtudiantController controller) {
        this.etudiantController = controller;
    }

    /**
     * Sets the Etudiant to edit and populates the form fields.
     *
     * @param etudiant The Etudiant object to edit.
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
        populateFields();
    }

    /**
     * Populates the form fields with the student's current details.
     */
    private void populateFields() {
        if (etudiant != null) {
            matriculeField.setText(etudiant.getMatricule());
            nomField.setText(etudiant.getNom());
            prenomField.setText(etudiant.getPrenom());
            dateNaissanceField.setText(etudiant.getDateNaissance());
            emailField.setText(etudiant.getEmail());
            promotionField.setText(etudiant.getPromotion());
        }
    }

    /**
     * Handles the action of updating the student.
     */
    @FXML
    private void handleUpdate() throws SQLException {
        if (isInputValid()) {
            etudiant.setMatricule(matriculeField.getText());
            etudiant.setNom(nomField.getText());
            etudiant.setPrenom(prenomField.getText());
            etudiant.setDateNaissance(dateNaissanceField.getText());
            etudiant.setEmail(emailField.getText());
            etudiant.setPromotion(promotionField.getText());

            // Update in database
            etudiantDAO.update(etudiant);
            System.out.println("Updated student: " + etudiant); // Debugging

            // Close the popup
            Stage stage = (Stage) matriculeField.getScene().getWindow();
            stage.close();

            // Refresh the main table view
            if (etudiantController != null) {
                etudiantController.loadEtudiants();
            }

            // Show success alert
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant mis à jour avec succès!");
        }
    }

    /**
     * Handles the action of canceling the update operation.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) matriculeField.getScene().getWindow();
        stage.close();
    }

    /**
     * Validates the user input in the form fields.
     *
     * @return true if the input is valid, false otherwise.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (matriculeField.getText() == null || matriculeField.getText().trim().isEmpty()) {
            errorMessage += "Matricule invalide!\n";
        }
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage += "Nom invalide!\n";
        }
        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            errorMessage += "Prénom invalide!\n";
        }
        if (dateNaissanceField.getText() == null || !dateNaissanceField.getText().matches("\\d{4}-\\d{2}-\\d{2}")) {
            errorMessage += "Date de naissance invalide! Utilisez le format YYYY-MM-DD.\n";
        }
        if (emailField.getText() == null || !emailField.getText().matches("^\\S+@\\S+\\.\\S+$")) {
            errorMessage += "Email invalide!\n";
        }
        if (promotionField.getText() == null || promotionField.getText().trim().isEmpty()) {
            errorMessage += "Promotion invalide!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Entrée invalide", errorMessage);
            return false;
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
