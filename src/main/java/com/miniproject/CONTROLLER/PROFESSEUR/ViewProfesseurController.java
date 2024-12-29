package com.miniproject.CONTROLLER.PROFESSEUR;

import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Professeur;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ViewProfesseurController {
    @FXML
    private Label matriculeLabel;
    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label specialiteLabel;

    private Professeur professeur;

    /**
     * Sets the Professeur object to display.
     *
     * @param professeur The Professeur instance.
     */
    public void setProfesseur(Professeur professeur) {
        this.professeur = professeur;
        populateFields();
    }

    /**
     * Populates the labels with the student's details.
     */
    private void populateFields() {
        if (professeur != null) {
            nomLabel.setText(professeur.getNom());
            prenomLabel.setText(professeur.getPrenom());
            specialiteLabel.setText(professeur.getSpecialite());
        }
    }

    /**
     * Handles closing the pop-up.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) nomLabel.getScene().getWindow();
        stage.close();
    }
}
