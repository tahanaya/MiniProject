package com.miniproject.CONTROLLER.ETUDIANT;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.ENTITY.Etudiant;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Controller class for adding a new Etudiant (Student).
 * Handles user input, validation, and interaction with the DAO to persist data.
 */
public class AddStudentController {

    // FXML-injected UI components
    @FXML private TextField matriculeField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField dateNaissanceField;
    @FXML private TextField emailField;
    @FXML private TextField promotionField;

    // Reference to EtudiantController to refresh data
    private EtudiantController etudiantController;

    // Data Access Object for Etudiant operations
    private final GenericDAO<Etudiant> etudiantDAO = new EtudiantDAOImpl();

    /**
     * Sets the reference to the main EtudiantController.
     *
     * @param controller The EtudiantController instance.
     */
    public void setEtudiantController(EtudiantController controller) {
        this.etudiantController = controller;
    }

    /**
     * Handles the action of adding a new student.
     * Validates input, persists the new student, and refreshes the main table view.
     */
    @FXML
    private void handleAdd() {
        if (isInputValid()) {
            Etudiant newEtudiant = createEtudiantFromInput();
            etudiantDAO.save(newEtudiant);
            System.out.println("Added student: " + newEtudiant); // Debugging statement

            closeWindow();
            refreshMainController();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully!");
        }
    }

    /**
     * Handles the action of canceling the add operation.
     * Closes the current window without saving.
     */
    @FXML
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Creates an Etudiant object from the user input fields.
     *
     * @return A populated Etudiant instance.
     */
    private Etudiant createEtudiantFromInput() {
        Etudiant etudiant = new Etudiant();
        etudiant.setMatricule(matriculeField.getText().trim());
        etudiant.setNom(nomField.getText().trim());
        etudiant.setPrenom(prenomField.getText().trim());
        etudiant.setDateNaissance(dateNaissanceField.getText().trim());
        etudiant.setEmail(emailField.getText().trim());
        etudiant.setPromotion(promotionField.getText().trim());
        return etudiant;
    }

    /**
     * Validates the user input in the form fields.
     *
     * @return true if the input is valid; false otherwise.
     */
    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (isFieldEmpty(matriculeField)) {
            errorMessage.append("Invalid Matricule!\n");
        }
        if (isFieldEmpty(nomField)) {
            errorMessage.append("Invalid Nom!\n");
        }
        if (isFieldEmpty(prenomField)) {
            errorMessage.append("Invalid Prenom!\n");
        }
        if (!isValidDate(dateNaissanceField.getText())) {
            errorMessage.append("Invalid Date of Birth! Use format YYYY-MM-DD.\n");
        }
        if (!isValidEmail(emailField.getText())) {
            errorMessage.append("Invalid Email!\n");
        }
        if (isFieldEmpty(promotionField)) {
            errorMessage.append("Invalid Promotion!\n");
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", errorMessage.toString());
            return false;
        }
    }

    /**
     * Checks if a TextField is empty or contains only whitespace.
     *
     * @param field The TextField to check.
     * @return true if empty; false otherwise.
     */
    private boolean isFieldEmpty(TextField field) {
        return field.getText() == null || field.getText().trim().isEmpty();
    }

    /**
     * Validates the date string against the YYYY-MM-DD format.
     *
     * @param date The date string to validate.
     * @return true if valid; false otherwise.
     */
    private boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    /**
     * Validates the email string against a basic email pattern.
     *
     * @param email The email string to validate.
     * @return true if valid; false otherwise.
     */
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^\\S+@\\S+\\.\\S+$");
    }

    /**
     * Closes the current window.
     */
    private void closeWindow() {
        Stage stage = (Stage) matriculeField.getScene().getWindow();
        stage.close();
    }

    /**
     * Refreshes the main EtudiantController to reflect the new data.
     */
    private void refreshMainController() {
        if (etudiantController != null) {
            etudiantController.loadEtudiants();
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
