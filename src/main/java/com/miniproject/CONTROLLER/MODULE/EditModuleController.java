package com.miniproject.CONTROLLER.MODULE;

import com.miniproject.DAO.ModuleDAOImpl;
import com.miniproject.DAO.ProfesseurDAOImp;
import com.miniproject.ENTITY.Module;
import com.miniproject.ENTITY.Professeur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditModuleController {

    @FXML private TextField nameField;
    @FXML private TextField codeField;
    @FXML private ComboBox<Professeur> professorComboBox;

    private Module module;
    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
    private final ProfesseurDAOImp professeurDAO = new ProfesseurDAOImp();

    @FXML
    public void initialize() {
        loadProfessors(); // Populate the ComboBox with professors
    }
    private void loadProfessors() {
        ObservableList<Professeur> professorsList = FXCollections.observableArrayList(professeurDAO.findAll());
        professorComboBox.setItems(professorsList);

        // Customize display of professors in the ComboBox
        professorComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Professeur item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getUtilisateur() == null) {
                    setText(null);
                } else {
                    setText(item.getUtilisateur().getNom() + " " + item.getUtilisateur().getPrenom());
                }
            }
        });
        professorComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Professeur item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.getUtilisateur() == null) {
                    setText(null);
                } else {
                    setText(item.getUtilisateur().getNom() + " " + item.getUtilisateur().getPrenom());
                }
            }
        });
    }

    public void setModule(Module module) {
        this.module = module;
        // Populate fields with module details
        nameField.setText(module.getNomModule());
        codeField.setText(module.getCodeModule());
        // Select the current professor in the ComboBox
        if (module.getProfesseur() != null) {
            professorComboBox.setValue(module.getProfesseur());
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            module.setNomModule(nameField.getText());
            module.setCodeModule(codeField.getText());
            module.setProfesseur(professorComboBox.getValue());

            // Update the module in the database
            moduleDAO.update(module);
            closeWindow();
        }
    }
    @FXML
    private void handleCancel() {
        closeWindow();
    }
    private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().isEmpty()) {
            errorMessage += "Nom du Module invalide!\n";
        }
        if (codeField.getText() == null || codeField.getText().isEmpty()) {
            errorMessage += "Code du Module invalide!\n";
        }
        if (professorComboBox.getValue() == null) {
            errorMessage += "Veuillez choisir un professeur associé!\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Erreur de Validation", errorMessage);
            return false;
        }
    }
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
