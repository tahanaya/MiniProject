package com.miniproject.CONTROLLER.EtudiantProfesseur;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.ENTITY.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
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

public class EtudiantProfesseurController {

    // FXML-injected TableView and TableColumns
    @FXML
    private TableView<Etudiant> etudiantTable;
    @FXML
    private TableColumn<Etudiant, Integer> colId;
    @FXML
    private TableColumn<Etudiant, String> colMatricule;
    @FXML
    private TableColumn<Etudiant, String> colNom;
    @FXML
    private TableColumn<Etudiant, String> colPrenom;
    @FXML
    private TableColumn<Etudiant, String> colDateNaissance;
    @FXML
    private TableColumn<Etudiant, String> colEmail;
    @FXML
    private TableColumn<Etudiant, String> colPromotion;
    @FXML
    private TableColumn<Etudiant, Void> colVisualiser;

    @FXML private TextField searchBar;

    // DAO for Etudiant operations
    private final GenericDAO<Etudiant> etudiantDAO = new EtudiantDAOImpl();

    // ObservableList to hold Etudiant data for the TableView
    private final ObservableList<Etudiant> etudiantList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("EtudiantProfesseurController initialized.");
        setupTableColumns();
        loadEtudiants();
        setupActionButtons();
        setupSearchFunctionality();
        etudiantTable.setItems(etudiantList);
    }

    /**
     * Sets up the table columns with appropriate cell value factories.
     */
    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPromotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));
    }

    /**
     * Loads all students from the database into the TableView.
     */
    private void loadEtudiants() {
        etudiantList.clear();
        List<Etudiant> list = etudiantDAO.findAll();
        System.out.println("Nombre d'étudiants récupérés : " + list.size());
        etudiantList.addAll(list);
    }

    /**
     * Sets up action buttons in the table columns.
     */
    private void setupActionButtons() {
        addButtonToColumn(colVisualiser, "/com/miniproject/images/visualize-icon.png", "Visualiser", this::handleViewStudent);
    }

    /**
     * Adds a button to a table column with an icon, tooltip, and action.
     *
     * @param column      The TableColumn to which the button will be added.
     * @param iconPath    The path to the icon image.
     * @param tooltipText The tooltip text for the button.
     * @param action      The action to perform when the button is clicked.
     */
    private void addButtonToColumn(TableColumn<Etudiant, Void> column, String iconPath, String tooltipText, Consumer<Etudiant> action) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = createIconButton(iconPath, tooltipText);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Etudiant etudiant = getTableView().getItems().get(getIndex());
                    button.setOnAction(event -> action.accept(etudiant));
                    setGraphic(button);
                }
            }
        });
    }

    /**
     * Creates a styled button with an icon and tooltip.
     *
     * @param iconPath    The path to the icon image.
     * @param tooltipText The tooltip text for the button.
     * @return The configured Button instance.
     */
    private Button createIconButton(String iconPath, String tooltipText) {
        Button button = new Button();
        try {
            ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
            icon.setFitHeight(20);
            icon.setFitWidth(20);
            button.setGraphic(icon);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'icône : " + iconPath);
        }
        button.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        button.setTooltip(new Tooltip(tooltipText));
        return button;
    }

    /**
     * Handles viewing the selected student's details.
     */
    private void handleViewStudent(Etudiant etudiant) {
        if (etudiant != null) {
            openPopup("/com/miniproject/view/Etudiant-Professeur/viewEtudiantProfesseur.fxml", "Détails de l'étudiant", controller -> {
                if (controller instanceof viewEtudiantProfesseurController viewController) {
                    viewController.setEtudiant(etudiant);
                }
            });
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à visualiser.");
        }
    }

    /**
     * Opens a popup window with the specified FXML, title, and controller setup.
     *
     * @param fxmlPath        Le chemin vers le fichier FXML.
     * @param title           Le titre de la fenêtre popup.
     * @param controllerSetup Une lambda pour configurer le contrôleur.
     */
    private void openPopup(String fxmlPath, String title, Consumer<Object> controllerSetup) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (controllerSetup != null) {
                controllerSetup.accept(loader.getController());
            }

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root));
            popupStage.setTitle(title);
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.setMinWidth(200);
            popupStage.setMinHeight(300);
            popupStage.centerOnScreen();
            popupStage.showAndWait();

            loadEtudiants(); // Rafraîchir les données après fermeture du popup

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de visualisation.");
        }
    }

    /**
     * Affiche une alerte à l'utilisateur.
     *
     * @param type    Le type d'alerte.
     * @param title   Le titre de l'alerte.
     * @param message Le message de l'alerte.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Met en place la fonctionnalité de recherche pour filtrer les étudiants.
     */
    private void setupSearchFunctionality() {
        FilteredList<Etudiant> filteredList = new FilteredList<>(etudiantList, p -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            String lowerCaseFilter = (newValue != null) ? newValue.toLowerCase() : "";

            filteredList.setPredicate(etudiant -> {
                if (lowerCaseFilter.isEmpty()) {
                    return true;
                }
                return matchesFilter(etudiant, lowerCaseFilter);
            });
        });

        etudiantTable.setItems(filteredList);
    }

    /**
     * Vérifie si un étudiant correspond au filtre de recherche.
     *
     * @param etudiant         L'étudiant à vérifier.
     * @param lowerCaseFilter  Le filtre de recherche en minuscules.
     * @return Vrai si l'étudiant correspond au filtre ; sinon, faux.
     */
    private boolean matchesFilter(Etudiant etudiant, String lowerCaseFilter) {
        return containsIgnoreCase(etudiant.getMatricule(), lowerCaseFilter) ||
                containsIgnoreCase(etudiant.getNom(), lowerCaseFilter) ||
                containsIgnoreCase(etudiant.getPrenom(), lowerCaseFilter) ||
                containsIgnoreCase(etudiant.getDateNaissance(), lowerCaseFilter) ||
                containsIgnoreCase(etudiant.getEmail(), lowerCaseFilter) ||
                containsIgnoreCase(etudiant.getPromotion(), lowerCaseFilter);
    }

    /**
     * Vérifie si une chaîne contient le filtre, sans tenir compte de la casse.
     *
     * @param field            La chaîne à vérifier.
     * @param lowerCaseFilter  Le filtre en minuscules.
     * @return Vrai si la chaîne contient le filtre ; sinon, faux.
     */
    private boolean containsIgnoreCase(String field, String lowerCaseFilter) {
        return field != null && field.toLowerCase().contains(lowerCaseFilter);
    }

    public void handleSearch(ActionEvent actionEvent) {
    }
}
