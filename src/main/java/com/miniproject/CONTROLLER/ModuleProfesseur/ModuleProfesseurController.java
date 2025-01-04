package com.miniproject.CONTROLLER.ModuleProfesseur;

import com.miniproject.DAO.ModuleDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.ENTITY.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ModuleProfesseurController {

    // FXML-injected TableView and TableColumns
    @FXML
    private TableView<Module> moduleTable;
    @FXML
    private TableColumn<Module, Integer> colId;
    @FXML
    private TableColumn<Module, String> colCode;
    @FXML
    private TableColumn<Module, String> colName;
    @FXML
    private TableColumn<Module, String> colProfessorFullName;

    // DAO for Module operations
    private final GenericDAO<Module> moduleDAO = new ModuleDAOImpl();

    // ObservableList to hold Module data for the TableView
    private final ObservableList<Module> moduleList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("ModuleProfesseurController initialized."); // Debugging statement

        // Initialize table columns with PropertyValueFactory
        setupTableColumns();

        // Load module data from the DAO
        loadModules();

        // Bind the data to the TableView
        moduleTable.setItems(moduleList);
    }

    /**
     * Sets up the table columns with appropriate cell value factories.
     */
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("codeModule"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nomModule"));
        colProfessorFullName.setCellValueFactory(new PropertyValueFactory<>("professorFullName"));
    }

    /**
     * Loads all modules from the database into the TableView.
     */
    private void loadModules() {
        moduleList.clear(); // Clear existing data
        List<Module> list = moduleDAO.findAll(); // Fetch data from DAO
        System.out.println("Number of modules fetched: " + list.size()); // Debugging statement
        moduleList.addAll(list); // Add data to ObservableList
    }

    /**
     * Handles viewing the selected module's details.
     */
    @FXML
    private void handleViewModule() {
        Module selected = moduleTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openViewModulePopup(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un module à visualiser.");
        }
    }

    /**
     * Opens the View Module popup window for the selected module.
     *
     * @param module The selected Module to view.
     */
    private void openViewModulePopup(Module module) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Module-Professeur/viewModuleProfesseur.fxml"));
            Parent root = loader.load();

            // Get the controller of the View Module popup and pass the selected module
            ViewModuleProfesseurController viewModuleController = loader.getController();
            viewModuleController.setModule(module);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 300));
            popupStage.setTitle("Détails du module");
            popupStage.initOwner(moduleTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de visualisation.");
        }
    }

    /**
     * Shows an alert to the user.
     *
     * @param type    The type of the alert.
     * @param title   The title of the alert.
     * @param message The message to display in the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
