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

public class AddProfesseurController {

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
    private Button saveButton;
    @FXML
    private Button cancelButton;

    // Reference to ProfesseurController to refresh data
    private ProfesseurController professeurController;

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
     * Handles the action of adding a new professor.
     */
    @FXML
    public void handleAdd() {
        if (isInputValid()) {
            try {
                // Create a new Utilisateur
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setUsername(usernameField.getText());
                utilisateur.setPassword(passwordField.getText());
                utilisateur.setNom(nomField.getText());
                utilisateur.setPrenom(prenomField.getText());
                utilisateur.setRole("PROFESSEUR");  // Set role as "PROFESSEUR"

                // Save the Utilisateur to the database
                utilisateurDAO.addUser(utilisateur);

                // Log Utilisateur ID after insertion
                System.out.println("Utilisateur ID after saving: " + utilisateur.getId());

                // Create the Professeur object and associate Utilisateur
                Professeur newProf = new Professeur();
                newProf.setSpecialite(specialiteField.getText());
                newProf.setUtilisateur(utilisateur);  // Associate Utilisateur

                // Save the Professeur to the database
                professeurDAO.save(newProf);
                System.out.println("Added Professeur: " + newProf); // Debugging

                // Log the ID of the Professeur after saving
                System.out.println("Professeur ID after saving: " + newProf.getId());

                // Close the popup
                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.close();

                // Refresh the main table view
                if (professeurController != null) {
                    professeurController.loadProfesseurs();
                }

                // Show success alert
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Professeur ajouté avec succès!");

                // Clear form after successful addition
                clearForm();
            } catch (Exception e) {
                // Check if the exception is due to role constraint violation
                if (e.getMessage().contains("utilisateur_role_check")) {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Le rôle doit être ADMINISTRATEUR, SECRETAIRE ou PROFESSEUR.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de l'ajout du professeur.");
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles the action of canceling the add operation.
     */
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) nomField.getScene().getWindow();
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

    /**
     * Clears the form fields.
     */
    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        specialiteField.clear();
        usernameField.clear();
        passwordField.clear();
    }
}
