package com.miniproject.CONTROLLER.EtudiantProfesseur;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.ENTITY.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
        System.out.println("EtudiantController initialized.");
        setupTableColumns(); // Configure les colonnes normales
        loadEtudiants();     // Charge les données
        setupActionButtons(); // Configure les colonnes avec des boutons
        etudiantTable.setItems(etudiantList); // Associe les données
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
    void loadEtudiants() {
        etudiantList.clear(); // Clear existing data
        List<Etudiant> list = etudiantDAO.findAll(); // Fetch data from DAO
        System.out.println("Number of students fetched: " + list.size()); // Debugging statement
        etudiantList.addAll(list); // Add data to ObservableList
    }

    /**
     * Handles viewing the selected student's details.
     */
    @FXML
    private void handleViewStudent() {
        Etudiant selected = etudiantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            handleViewStudent(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à visualiser.");
        }
    }

    /**
     * Opens the View Student popup window for the selected student.
     *
     * @param etudiant The selected Etudiant to view.
     */
    private void handleViewStudent(Etudiant etudiant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant-Professeur/viewEtudiantProfesseur.fxml"));
            Parent root = loader.load();

            // Get the controller of the View Student popup and pass the selected student
            viewEtudiantProfesseurController viewStudentController = loader.getController();
            viewStudentController.setEtudiant(etudiant);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 300));
            popupStage.setTitle("Détails de l'étudiant");
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

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
    @FXML
    private void handleSearch() {
        String searchText = searchBar.getText().toLowerCase();


        // Si la barre de recherche est vide, afficher tous les étudiants
        if (searchText.isEmpty()) {
            etudiantTable.setItems(etudiantList);
            return;
        }

        // Filtrer la liste des étudiants
        FilteredList<Etudiant> filteredList = new FilteredList<>(etudiantList, etudiant -> {
//            if (searchText == null || searchText.isEmpty()) {
//                return true;
//            }

            // Vérifier si n'importe quel attribut correspond au texte recherché
            return (etudiant.getMatricule() != null && etudiant.getMatricule().toLowerCase().contains(searchText)) ||
                    (etudiant.getNom() != null && etudiant.getNom().toLowerCase().contains(searchText)) ||
                    (etudiant.getPrenom() != null && etudiant.getPrenom().toLowerCase().contains(searchText)) ||
                    (etudiant.getDateNaissance() != null && etudiant.getDateNaissance().toLowerCase().contains(searchText)) ||
                    (etudiant.getEmail() != null && etudiant.getEmail().toLowerCase().contains(searchText)) ||
                    (etudiant.getPromotion() != null && etudiant.getPromotion().toLowerCase().contains(searchText));
        });

        // Appliquer la liste filtrée à la TableView
        etudiantTable.setItems(filteredList);
    }

    private void setupActionButtons() {
        addButtonToColumn(colVisualiser, "/com/miniproject/images/visualize-icon.png", "Visualiser", this::handleViewStudent);
      }

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


}
