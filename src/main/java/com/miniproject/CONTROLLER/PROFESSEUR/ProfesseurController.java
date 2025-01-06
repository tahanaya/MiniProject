package com.miniproject.CONTROLLER.PROFESSEUR;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.DAO.ProfesseurDAOImpl;
import com.miniproject.ENTITY.Professeur;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Controller class for managing Professeur (Professor) records.
 * Provides functionalities to view, add, edit, delete, search, and export professor data.
 */
public class ProfesseurController {

    // FXML-injected UI components
    @FXML private TableView<Professeur> professeurTable;
    @FXML private TableColumn<Professeur, Integer> colId;
    @FXML private TableColumn<Professeur, String> colNom;
    @FXML private TableColumn<Professeur, String> colPrenom;
    @FXML private TableColumn<Professeur, String> colSpecialite;
    @FXML private TextField searchBar;
    @FXML private TableColumn<Professeur, Void> colVisualiser;
    @FXML private TableColumn<Professeur, Void> colModifier;
    @FXML private TableColumn<Professeur, Void> colSupprimer;

    // DAO for Professeur
    private final GenericDAO<Professeur> professeurDAO = new ProfesseurDAOImpl();

    // ObservableList for TableView
    private final ObservableList<Professeur> professeurList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        initializeTableColumns();
        loadProfesseurs();
        setupActionButtons();
        setupSearchFunctionality();
    }

    /**
     * Sets up the table columns with appropriate cell value factories.
     */
    private void initializeTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colSpecialite.setCellValueFactory(new PropertyValueFactory<>("specialite"));
    }

    /**
     * Loads all professors from the database into the TableView.
     */
    void loadProfesseurs() {
        professeurList.clear();
        List<Professeur> list = professeurDAO.findAll();
        professeurList.addAll(list);
        professeurTable.setItems(professeurList);
    }

    /**
     * Sets up action buttons in the table columns.
     */
    private void setupActionButtons() {
        addButtonToColumn(colVisualiser, "/com/miniproject/images/visualize-icon.png", "Visualiser", this::handleViewProfesseur);
        addButtonToColumn(colModifier, "/com/miniproject/images/modify-icon.png", "Modifier", this::handleEditProfesseur);
        addButtonToColumn(colSupprimer, "/com/miniproject/images/delete-icon.png", "Supprimer", this::handleDeleteProfesseur);
    }

    /**
     * Adds a button to a table column with an icon, tooltip, and action.
     *
     * @param column      The TableColumn to which the button will be added.
     * @param iconPath    The path to the icon image.
     * @param tooltipText The tooltip text for the button.
     * @param action      The action to perform when the button is clicked.
     */
    private void addButtonToColumn(TableColumn<Professeur, Void> column, String iconPath, String tooltipText, Consumer<Professeur> action) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = createIconButton(iconPath, tooltipText);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Professeur professeur = getTableView().getItems().get(getIndex());
                    button.setOnAction(event -> action.accept(professeur));
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
            System.err.println("Error loading icon: " + iconPath);
        }
        button.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        button.setTooltip(new Tooltip(tooltipText));
        return button;
    }

    /**
     * Sets up the search functionality to filter the TableView based on user input.
     */
    private void setupSearchFunctionality() {
        FilteredList<Professeur> filteredList = new FilteredList<>(professeurList, p -> true);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            String lowerCaseFilter = (newValue != null) ? newValue.toLowerCase() : "";

            filteredList.setPredicate(professeur -> {
                if (lowerCaseFilter.isEmpty()) {
                    return true;
                }
                return matchesFilter(professeur, lowerCaseFilter);
            });
        });

        professeurTable.setItems(filteredList);
    }

    /**
     * Checks if a Professeur matches the search filter.
     *
     * @param professeur       The Professeur to check.
     * @param lowerCaseFilter  The search filter in lowercase.
     * @return True if the Professeur matches the filter; otherwise, false.
     */
    private boolean matchesFilter(Professeur professeur, String lowerCaseFilter) {
        return containsIgnoreCase(professeur.getNom(), lowerCaseFilter) ||
                containsIgnoreCase(professeur.getPrenom(), lowerCaseFilter) ||
                containsIgnoreCase(professeur.getSpecialite(), lowerCaseFilter);
    }

    /**
     * Checks if a string contains the filter text, ignoring case.
     *
     * @param field            The string field to check.
     * @param lowerCaseFilter  The filter text in lowercase.
     * @return True if the field contains the filter text; otherwise, false.
     */
    private boolean containsIgnoreCase(String field, String lowerCaseFilter) {
        return field != null && field.toLowerCase().contains(lowerCaseFilter);
    }

    /**
     * Opens a popup window for adding, editing, or viewing a professor.
     *
     * @param fxmlPath         The path to the FXML file.
     * @param title            The title of the popup window.
     * @param controllerSetup  A consumer to set up the controller.
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
            popupStage.initOwner(professeurTable.getScene().getWindow());
            popupStage.setMinWidth(500);
            popupStage.setMinHeight(400);
            popupStage.centerOnScreen();
            popupStage.showAndWait();

            loadProfesseurs(); // Refresh data after popup closes

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre.");
        }
    }

    /**
     * Handles adding a new professor.
     */
    @FXML
    private void handleAddProfesseur() {
        openPopup("/com/miniproject/view/Professeur/add-professeur.fxml", "Ajouter un Professeur", controller -> {
            if (controller instanceof AddProfesseurController addController) {
                addController.setProfesseurController(this);
            }
        });
    }

    /**
     * Handles editing an existing professor.
     *
     * @param professeur The selected Professeur to edit.
     */
    private void handleEditProfesseur(Professeur professeur) {
        if (professeur == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un professeur à modifier.");
            return;
        }

        openPopup("/com/miniproject/view/Professeur/edit-professeur.fxml", "Modifier un Professeur", controller -> {
            if (controller instanceof EditProfesseurController editController) {
                editController.setProfesseurController(this);
                editController.setProfesseur(professeur);
            }
        });
    }

    /**
     * Handles viewing the details of a professor.
     *
     * @param professeur The selected Professeur to view.
     */
    private void handleViewProfesseur(Professeur professeur) {
        if (professeur == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un professeur à visualiser.");
            return;
        }

        openPopup("/com/miniproject/view/Professeur/view-professeur.fxml", "Visualiser un Professeur", controller -> {
            if (controller instanceof ViewProfesseurController viewController) {
                viewController.setProfesseur(professeur);
            }
        });
    }

    /**
     * Handles deleting a selected professor.
     *
     * @param professeur The selected Professeur to delete.
     */
    private void handleDeleteProfesseur(Professeur professeur) {
        if (professeur == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un professeur à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer Professeur");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce professeur?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            professeurDAO.delete(professeur.getId());
            loadProfesseurs();
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Professeur supprimé avec succès!");
        }
    }

    /**
     * Handles exporting the list of professors to a CSV file.
     */
    @FXML
    private void handleExportProfesseursCSV() {
        // Open a FileChooser to select the destination file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les Professeurs en CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show save dialog
        File file = fileChooser.showSaveDialog(professeurTable.getScene().getWindow());
        if (file != null) {
            exportToCSV(file);
        }
    }

    /**
     * Exports the list of professors to a CSV file using standard Java I/O.
     *
     * @param file The destination file.
     */
    private void exportToCSV(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write CSV header
            writer.write("ID,Nom,Prenom,Specialite");
            writer.newLine();

            // Write professor data
            for (Professeur professeur : professeurList) {
                String id = String.valueOf(professeur.getId());
                String nom = escapeSpecialCharacters(professeur.getNom());
                String prenom = escapeSpecialCharacters(professeur.getPrenom());
                String specialite = escapeSpecialCharacters(professeur.getSpecialite());

                String line = String.format("%s,%s,%s,%s", id, nom, prenom, specialite);
                writer.write(line);
                writer.newLine();
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les professeurs ont été exportés en CSV avec succès!");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'exportation en CSV.");
        }
    }

    /**
     * Escapes special characters in CSV fields.
     * Encloses the field in quotes if it contains commas, quotes, or newlines.
     * Doubles any existing quotes within the field.
     *
     * @param data The data string to escape.
     * @return The escaped data string.
     */
    private String escapeSpecialCharacters(String data) {
        String escapedData = data;
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    /**
     * Handles exporting the list of professors to a PDF file.
     */
    @FXML
    private void handleExportProfesseursPDF() {
        // Open a FileChooser to select the destination file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les Professeurs en PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(professeurTable.getScene().getWindow());
        if (file != null) {
            exportToPDF(file);
        }
    }

    /**
     * Exports the list of professors to a PDF file using iText.
     *
     * @param file The destination file.
     */
    private void exportToPDF(File file) {
        try {
            // Load custom font
            String fontPath = "src/main/resources/com/miniproject/Fonts/Times New Roman.ttf"; // Update path as needed
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H);

            // Initialize PDF document and Writer
            PdfWriter writer = new PdfWriter(file);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Add title
            document.add(new Paragraph("Liste des Professeurs")
                    .setBold()
                    .setFontSize(16)
                    .setMarginLeft(50)
                    .setMarginBottom(10)
                    .setMarginTop(10)
                    .setFont(font));

            // Create a table with appropriate column count
            Table table = new Table(new float[]{1, 3, 3, 3});
            table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            // Add table headers
            table.addHeaderCell("ID").setFont(font);
            table.addHeaderCell("Nom").setFont(font);
            table.addHeaderCell("Prénom").setFont(font);
            table.addHeaderCell("Spécialité").setFont(font);

            // Add professor data to the table
            for (Professeur professeur : professeurList) {
                table.addCell(String.valueOf(professeur.getId())).setFont(font);
                table.addCell(professeur.getNom()).setFont(font);
                table.addCell(professeur.getPrenom()).setFont(font);
                table.addCell(professeur.getSpecialite()).setFont(font);
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les professeurs ont été exportés en PDF avec succès!");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de l'exportation en PDF.");
        }
    }

    /**
     * Displays an alert dialog to the user.
     *
     * @param type    The type of alert.
     * @param title   The title of the alert.
     * @param message The message content of the alert.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles searching within the professor table.
     */
    @FXML
    private void handleSearch() {
        String searchText = searchBar.getText().toLowerCase();

        if (searchText.isEmpty()) {
            professeurTable.setItems(professeurList);
            return;
        }

        FilteredList<Professeur> filteredList = new FilteredList<>(professeurList, professeur -> {
            return (professeur.getNom() != null && professeur.getNom().toLowerCase().contains(searchText)) ||
                    (professeur.getPrenom() != null && professeur.getPrenom().toLowerCase().contains(searchText)) ||
                    (professeur.getSpecialite() != null && professeur.getSpecialite().toLowerCase().contains(searchText));
        });

        professeurTable.setItems(filteredList);
    }
}
