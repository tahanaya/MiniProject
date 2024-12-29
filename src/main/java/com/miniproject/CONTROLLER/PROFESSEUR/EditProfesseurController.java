package com.miniproject.CONTROLLER.PROFESSEUR;

import com.miniproject.DAO.GenericDAO;
import com.miniproject.DAO.ProfesseurDAOImp;
import com.miniproject.ENTITY.Professeur;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditProfesseurController {
    @FXML private TextField idField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField specialiteField;

    // Reference to ProfesseurController to refresh data
    private ProfesseurController professeurController;

    // The professor to edit
    private Professeur professeur;

    // DAO for Professeur
    private final GenericDAO<Professeur> professeurDAO = new ProfesseurDAOImp();

    /**
     * Sets the ProfesseurController reference.
     *
     * @param controller The ProfesseurController instance.
     */
    public void setProfesseurController(ProfesseurController controller) {
        this.professeurController = controller;
    }

    /**
     * Sets the Professeur to edit and populates the form fields.
     *
     * @param professeur The Professeur object to edit.
     */
    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
        if (professeur != null) {
            idField.setText(String.valueOf(professeur.getId()));
            nomField.setText(professeur.getUtilisateur().getNom());
            prenomField.setText(professeur.getUtilisateur().getPrenom());
            specialiteField.setText(professeur.getSpecialite());
            idField.setDisable(true);
        }
    }

    /**
     * Populates the form fields with the professor's current details.
     */
    private void populateFields() {
        if (professeur != null) {
            nomField.setText(professeur.getNom());
            prenomField.setText(professeur.getPrenom());
            specialiteField.setText(professeur.getSpecialite());
            idField.setText(String.valueOf(professeur.getId()));
            idField.setDisable(true);  // Disable the ID field to prevent edits
        }
    }

    /**
     * Handles the action of updating the professor.
     */
    @FXML
    private void handleUpdate() {
        if (isInputValid()) {
            try {
                professeur.getUtilisateur().setNom(nomField.getText());
                professeur.getUtilisateur().setPrenom(prenomField.getText());
                professeur.getUtilisateur().setRole("Professeur");
                professeur.setSpecialite(specialiteField.getText());

                professeurDAO.update(professeur);

                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.close();
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Professeur mis à jour avec succès!");

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la mise à jour.");
            }
        }
    }






    /**
     * Handles the action of canceling the update operation.
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
        StringBuilder errorMessage = new StringBuilder();

        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            errorMessage.append("Nom invalide!\n");
        }
        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            errorMessage.append("Prénom invalide!\n");
        }
        if (specialiteField.getText() == null || specialiteField.getText().trim().isEmpty()) {
            errorMessage.append("Spécialité invalide!\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Entrée invalide", errorMessage.toString());
            return false;
        }
        return true;
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
