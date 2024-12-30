package com.miniproject.CONTROLLER.MODULE;

import com.miniproject.DAO.ModuleDAOImpl;
import com.miniproject.ENTITY.Module;
import com.miniproject.ENTITY.Professeur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.miniproject.DAO.ProfesseurDAOImp;

public class AddModuleController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField codeField;
    @FXML
    //private TextField professorIdField; // Field to input professor's ID
    private ComboBox<Professeur> professorComboBox; // Updated to ComboBox

    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
    private final ProfesseurDAOImp professeurDAO = new ProfesseurDAOImp(); // DAO for fetching Professeur

    private ObservableList<Professeur> professorsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadProfessors(); // Populate the ComboBox
    }
    private void loadProfessors() {
        professorsList.addAll(professeurDAO.findAll()); // Fetch all professors
        professorComboBox.setItems(professorsList); // Set data in ComboBox
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

        //  @FXML
//    private void handleSave() {
//        Module module = new Module();
//        module.setNomModule(nameField.getText());
//        module.setCodeModule(codeField.getText());
//        module.setProfesseurId(Integer.parseInt(professorField.getText()));
//
//        moduleDAO.save(module);
//        closeWindow();
//    }
  @FXML
  private void handleSave() {
      String name = nameField.getText();
      String code = codeField.getText();
      Professeur selectedProfessor = professorComboBox.getValue();

      // Input validation
      if (name.isEmpty() || code.isEmpty() || selectedProfessor == null) {
          showAlert("Validation Error", "All fields must be filled.");
          return;
      }

//      try {
//          int professorId = Integer.parseInt(professorIdText);
//
//          // Fetch the professor from the database
//          Professeur professor = professeurDAO.findById(professorId);
//          if (professor == null) {
//              showAlert("Error", "Professor with ID " + professorId + " does not exist.");
//              return;
//          }

          // Create and save the module
          Module module = new Module();
          module.setNomModule(name);
          module.setCodeModule(code);
      module.setProfesseur(selectedProfessor);

          moduleDAO.save(module);
          closeWindow();

//      } catch (NumberFormatException e) {
//          showAlert("Validation Error", "Professor ID must be a valid number.");
//      }
  }
    @FXML
    private void handleCancel() {
        closeWindow();
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


