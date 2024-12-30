package com.miniproject.CONTROLLER.PROFESSEUR;

import com.miniproject.DAO.GenericDAO;
import com.miniproject.DAO.ProfesseurDAOImpl;
import com.miniproject.DAO.UserDAO;
import com.miniproject.DAO.UserDAOImpl;
import com.miniproject.ENTITY.Professeur;
import com.miniproject.ENTITY.Utilisateur;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProfesseurController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField specialiteField;
    @FXML
    private TextField usernameField; // For username input
    @FXML
    private PasswordField passwordField; // For password input

    @FXML
    private Button saveButton;    // Added fx:id="saveButton"
    @FXML
    private Button cancelButton;  // Added fx:id="cancelButton"

    // Reference to ProfesseurController to refresh data
    private ProfesseurController professeurController;

    private Professeur professeur;

    // DAOs
    private final GenericDAO<Professeur> professeurDAO = new ProfesseurDAOImpl();
    private final UserDAO utilisateurDAO = new UserDAOImpl();

    /**
     * Sets the ProfesseurController reference.
     *
     * @param controller The ProfesseurController instance.
     */
    public void setProfesseurController(ProfesseurController controller) {
        this.professeurController = controller;
    }

    /**
     * Sets the Professeur to be edited.
     *
     * @param professeur The Professeur instance.
     */
    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
        populateFields();
    }

    /**
     * Populates the input fields with the Professeur's current data.
     */
    private void populateFields() {
        if (professeur != null && professeur.getUtilisateur() != null) {
            usernameField.setText(professeur.getUtilisateur().getUsername());
            passwordField.setText(professeur.getUtilisateur().getPassword());
            nomField.setText(professeur.getUtilisateur().getNom());
            prenomField.setText(professeur.getUtilisateur().getPrenom());
            specialiteField.setText(professeur.getSpecialite());
        }
    }

    /**
     * Handles the save action to update the Professeur.
     */
    @FXML
    private void handleSave() {
        if (isInputValid()) {
            try {
                // Update Utilisateur object
                Utilisateur utilisateur = professeur.getUtilisateur();
                utilisateur.setUsername(usernameField.getText());
                utilisateur.setPassword(passwordField.getText());
                utilisateur.setNom(nomField.getText());
                utilisateur.setPrenom(prenomField.getText());
                utilisateur.setRole("PROFESSEUR");  // Ensure role remains "PROFESSEUR"

                // Update Professeur object
                professeur.setSpecialite(specialiteField.getText());

                // Update Utilisateur in the database
                utilisateurDAO.updateUser(utilisateur);

                // Update Professeur in the database
                professeurDAO.update(professeur);
                System.out.println("Updated Professeur: " + professeur); // Debugging

                // Inform user
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Professeur mis à jour avec succès!");

                // Close the popup
                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();

                // Refresh the main table view
                if (professeurController != null) {
                    professeurController.loadProfesseurs();
                }
            } catch (Exception e) {
                // Check if the exception is due to role constraint violation
                if (e.getMessage().contains("utilisateur_role_check")) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle doit être ADMINISTRATEUR, SECRETAIRE ou PROFESSEUR.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la mise à jour du professeur.");
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the cancel action to close the popup without saving.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Validates the user input in the form fields.
     *
     * @return true if the input is valid, false otherwise.
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage += "Nom invalide!\n";
        }
        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            errorMessage += "Prénom invalide!\n";
        }
        if (specialiteField.getText() == null || specialiteField.getText().trim().isEmpty()) {
            errorMessage += "Spécialité invalide!\n";
        }
        if (usernameField.getText() == null || usernameField.getText().trim().isEmpty()) {
            errorMessage += "Nom d'utilisateur invalide!\n";
        }
        if (passwordField.getText() == null || passwordField.getText().trim().isEmpty()) {
            errorMessage += "Mot de passe invalide!\n";
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
