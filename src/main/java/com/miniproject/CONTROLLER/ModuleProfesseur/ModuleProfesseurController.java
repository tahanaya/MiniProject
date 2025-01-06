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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class ModuleProfesseurController {

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

    @FXML
    private TableColumn<Module, Void> colVisualiser;

    private final GenericDAO<Module> moduleDAO = new ModuleDAOImpl();

    private final ObservableList<Module> moduleList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("ModuleProfesseurController initialized.");

        setupTableColumns();
        setupActionButtons();

        loadModules();
        moduleTable.setItems(moduleList);
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("codeModule"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nomModule"));
        colProfessorFullName.setCellValueFactory(new PropertyValueFactory<>("professorFullName"));
    }

    private void setupActionButtons() {
        addButtonToColumn(colVisualiser, "/com/miniproject/images/visualize-icon.png", "Visualiser", this::handleViewModule);
    }

    private void addButtonToColumn(TableColumn<Module, Void> column, String iconPath, String tooltipText, Consumer<Module> action) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = createIconButton(iconPath, tooltipText);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Module module = getTableView().getItems().get(getIndex());
                    button.setOnAction(event -> action.accept(module));
                    setGraphic(button);
                }
            }
        });
    }

    private Button createIconButton(String iconPath, String tooltipText) {
        Button button = new Button();
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            button.setGraphic(icon);
        } catch (Exception e) {
            System.err.println("Error loading icon: " + iconPath);
        }
        button.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        button.setTooltip(new Tooltip(tooltipText));
        return button;
    }

    private void loadModules() {
        moduleList.clear();
        List<Module> list = moduleDAO.findAll();
        System.out.println("Number of modules fetched: " + list.size());
        moduleList.addAll(list);
    }

    @FXML
    private void handleViewModule(Module module) {
        if (module != null) {
            openViewModulePopup(module);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un module à visualiser.");
        }
    }

    private void openViewModulePopup(Module module) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Module-Professeur/viewModuleProfesseur.fxml"));
            Parent root = loader.load();

            ViewModuleProfesseurController viewModuleController = loader.getController();
            viewModuleController.setModule(module);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 300));
            popupStage.setTitle("Détails du module");
            popupStage.initOwner(moduleTable.getScene().getWindow());
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de visualisation.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
