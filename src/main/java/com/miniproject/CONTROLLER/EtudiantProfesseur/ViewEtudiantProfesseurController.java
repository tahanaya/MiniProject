package com.miniproject.CONTROLLER.EtudiantProfesseur;

import com.miniproject.ENTITY.Etudiant;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ViewEtudiantProfesseurController {

    @FXML
    private Label labelMatricule;
    @FXML
    private Label labelNom;
    @FXML
    private Label labelPrenom;
    @FXML
    private Label labelDateNaissance;
    @FXML
    private Label labelEmail;
    @FXML
    private Label labelPromotion;

    private Etudiant etudiant;

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;

        // Remplir les labels avec les données de l'étudiant
        labelMatricule.setText(etudiant.getMatricule());
        labelNom.setText(etudiant.getNom());
        labelPrenom.setText(etudiant.getPrenom());
        labelDateNaissance.setText(etudiant.getDateNaissance().toString());
        labelEmail.setText(etudiant.getEmail());
        labelPromotion.setText(etudiant.getPromotion());
    }

    @FXML
    private void handleClose() {
        // Fermer la fenêtre
        Stage stage = (Stage) labelMatricule.getScene().getWindow();
        stage.close();
    }
}
