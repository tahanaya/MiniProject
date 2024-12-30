package com.miniproject.CONTROLLER.MODULE;

import com.miniproject.DAO.ModuleDAOImpl;
import com.miniproject.ENTITY.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ModuleController {

    @FXML private TableView<Module> modulesTable;
    @FXML private TableColumn<Module, Integer> idColumn;
    @FXML private TableColumn<Module, String> nameColumn;
    @FXML private TableColumn<Module, String> codeColumn;
    @FXML private TableColumn<Module, Integer> professorColumn;
    @FXML private Label statusLabel;
    @FXML private TableColumn<Module, Void> associerEtudiantColumn;
    @FXML private TableColumn<Module, Void> etudiantsAssociesColumn;

    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
    private final ObservableList<Module> modulesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomModule"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("codeModule"));
        professorColumn.setCellValueFactory(new PropertyValueFactory<>("professorFullName"));

        // Ajouter les boutons dans les colonnes
        associerEtudiantColumn.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Associer");

            {
                button.setMaxWidth(Double.MAX_VALUE); // Allow button to grow with the column
                button.setOnAction(event -> {
                    Module module = getTableView().getItems().get(getIndex());
                    handleAssocierEtudiant(module);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Use a StackPane or HBox to ensure the button fills the cell
                    StackPane pane = new StackPane(button);
                    pane.setPrefWidth(getTableColumn().getWidth()); // Match the column width
                    setGraphic(pane);
                }
            }
        });
        etudiantsAssociesColumn.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button("Afficher");

            {
                button.setMaxWidth(Double.MAX_VALUE); // Allow button to grow with the column
                button.setOnAction(event -> {
                    Module module = getTableView().getItems().get(getIndex());
                    handleVoirEtudiants(module);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Use a StackPane or HBox to ensure the button fills the cell
                    StackPane pane = new StackPane(button);
                    pane.setPrefWidth(getTableColumn().getWidth()); // Match the column width
                    setGraphic(pane);                }
            }
        });
        // Load the modules from the database
        loadModules();
    }

    private void loadModules() {
        modulesList.clear();
        modulesList.addAll(moduleDAO.findAll());
        modulesTable.setItems(modulesList);
    }

    @FXML
    private void handleAdd() {
        try {
            // Load the AddModule.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Module/Add-module.fxml"));
            AnchorPane addModulePane = loader.load();

            // Open a new stage (modal dialog) for adding a module
            Stage addModuleStage = new Stage();
            addModuleStage.setTitle("Ajouter un Module");
            addModuleStage.initModality(Modality.APPLICATION_MODAL); // Block interaction with other windows
            addModuleStage.setScene(new Scene(addModulePane));
            addModuleStage.showAndWait();

            // Refresh the table after adding a module
            loadModules();
            showStatus("Module ajouté avec succès.", true); // Succès en vert
        } catch (IOException e) {
            showStatus("Erreur lors de l'ajout du module.", false); // Erreur en rouge
            e.printStackTrace();
        }
    }


    @FXML
    private void handleEdit() {
        Module selectedModule = modulesTable.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Module/Edit-module.fxml"));
                AnchorPane editModulePane = loader.load();

                // Pass the selected module to the controller
                EditModuleController controller = loader.getController();
                controller.setModule(selectedModule);

                // Open a new modal dialog
                Stage editStage = new Stage();
                editStage.setTitle("Modifier un Module");
                editStage.initModality(Modality.APPLICATION_MODAL);
                editStage.setScene(new Scene(editModulePane));
                editStage.showAndWait();

                // Refresh the table after editing the module
                loadModules();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Aucun module sélectionné", "Veuillez sélectionner un module à modifier.");
        }
    }

    @FXML
    private void handleDelete() {
        Module selectedModule = modulesTable.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            // Confirm before deleting
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer le module ?");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce module ?");
            if (confirmAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                // Delete the module
                moduleDAO.delete(selectedModule.getId());
                moduleDAO.reorganizeIds(); // Reorganize IDs after deletion
                loadModules();
                showStatus("Module supprimé avec succès.", true); // Succès en vert
            }
        } else {
            showStatus("Veuillez sélectionner un module à supprimer.", false); // Message en rouge
        }
    }

    @FXML
    private void handleAssocierEtudiant(Module module) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Module/AssocierEtudiant.fxml"));
            AnchorPane associerPane = loader.load();

            // Pass the selected module to the new controller
            AssocierEtudiantController controller = loader.getController();
            controller.setModule(module);

            Stage stage = new Stage();
            stage.setTitle("Associer un étudiant");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(associerPane));
            stage.showAndWait();

            loadModules(); // Refresh the modules table after association
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleVoirEtudiants(Module module) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Module/EtudiantsAssocies.fxml"));
            AnchorPane voirPane = loader.load();

            // Pass the selected module to the new controller
            EtudiantsAssociesController controller = loader.getController();
            controller.setModule(module);

            Stage stage = new Stage();
            stage.setTitle("Étudiants associés");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(voirPane));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Affiche un message dans le Label de statut.
     *
     * @param message Le message à afficher
     * @param isSuccess Vrai si l'opération est réussie, faux si elle échoue
     */
    private void showStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        if (isSuccess) {
            statusLabel.setStyle("-fx-text-fill: green; -fx-background-color: #e6ffe6; -fx-border-color: green; -fx-padding: 5px;");
        } else {
            statusLabel.setStyle("-fx-text-fill: red; -fx-background-color: #ffe6e6; -fx-border-color: red; -fx-padding: 5px;");
        }
    }


}
