package com.miniproject.CONTROLLER.ETUDIANT;

import com.miniproject.ENTITY.Etudiant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ViewStudentController {

    @FXML private Label matriculeLabel;
    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label dateNaissanceLabel;
    @FXML private Label emailLabel;
    @FXML private Label promotionLabel;

    private Etudiant etudiant;

    /**
     * Sets the Etudiant object to display.
     *
     * @param etudiant The Etudiant instance.
     */
    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
        populateFields();
    }

    /**
     * Populates the labels with the student's details.
     */
    private void populateFields() {
        if (etudiant != null) {
            matriculeLabel.setText(etudiant.getMatricule());
            nomLabel.setText(etudiant.getNom());
            prenomLabel.setText(etudiant.getPrenom());
            dateNaissanceLabel.setText(etudiant.getDateNaissance());
            emailLabel.setText(etudiant.getEmail());
            promotionLabel.setText(etudiant.getPromotion());
        }
    }

    /**
     * Handles closing the pop-up.
     */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) matriculeLabel.getScene().getWindow();
        stage.close();
    }
}
