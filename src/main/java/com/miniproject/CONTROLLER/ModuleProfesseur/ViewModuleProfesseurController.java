package com.miniproject.CONTROLLER.ModuleProfesseur;

import com.miniproject.ENTITY.Module;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ViewModuleProfesseurController {

    @FXML private Label moduleCodeLabel;
    @FXML private Label moduleNameLabel;
    @FXML private Label professorIdLabel;

    private Module module;

    /**
     * Sets the Module object to display.
     *
     * @param module The Module instance.
     */
    public void setModule(Module module) {
        this.module = module;
        populateFields();
    }

    /**
     * Populates the labels with the module's details.
     */
    private void populateFields() {
        if (module != null) {
            moduleCodeLabel.setText(module.getCodeModule());
            moduleNameLabel.setText(module.getNomModule());
            professorIdLabel.setText(module.getProfessorFullName());
        }
    }

    /**
     * Handles closing the pop-up.
     */
    @FXML
    private void handleBack() {
        Stage stage = (Stage) moduleCodeLabel.getScene().getWindow();
        stage.close();
    }
}
