package com.miniproject.CONTROLLER.ETUDIANT;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.GenericDAO;
import com.miniproject.DAO.InscriptionDAOImpl;
import com.miniproject.DAO.ModuleDAOImpl;
import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Module;

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
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.itextpdf.kernel.pdf.PdfWriter;


/**
 * Controller class for managing Etudiant (Student) records.
 * Provides functionalities to view, add, edit, delete, and export student data.
 */
public class EtudiantController {

    // FXML-injected UI components
    @FXML private TableView<Etudiant> etudiantTable;
    @FXML private TableColumn<Etudiant, Integer> colId;
    @FXML private TableColumn<Etudiant, String> colMatricule;
    @FXML private TableColumn<Etudiant, String> colNom;
    @FXML private TableColumn<Etudiant, String> colPrenom;
    @FXML private TableColumn<Etudiant, String> colDateNaissance;
    @FXML private TableColumn<Etudiant, String> colEmail;
    @FXML private TableColumn<Etudiant, String> colPromotion;
    @FXML private TextField searchBar;
    @FXML private TableColumn<Etudiant, Void> colModulesAssocies;
    @FXML private TableColumn<Etudiant, Void> colVisualiser;
    @FXML private TableColumn<Etudiant, Void> colModifier;
    @FXML private TableColumn<Etudiant, Void> colSupprimer;

    // Data Access Objects
    private final GenericDAO<Etudiant> etudiantDAO = new EtudiantDAOImpl();
    private final InscriptionDAOImpl inscriptionDAO = new InscriptionDAOImpl();
    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();

    // ObservableList to hold Etudiant data for the TableView
    private final ObservableList<Etudiant> etudiantList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        initializeTableColumns();
        loadEtudiants();
        setupActionButtons();
        setupSearchFunctionality();
    }

    /**
     * Sets up the table columns with appropriate cell value factories.
     */
    private void initializeTableColumns() {
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
        etudiantList.clear();
        List<Etudiant> list = etudiantDAO.findAll();
        etudiantList.addAll(list);
        etudiantTable.setItems(etudiantList);
    }

    /**
     * Sets up action buttons in the table columns.
     */
    private void setupActionButtons() {
        addButtonToColumn(colModulesAssocies, "/com/miniproject/images/modules-enrolled.png", "Modules Associés", this::handleViewEnrolledModules);
        addButtonToColumn(colVisualiser, "/com/miniproject/images/visualize-icon.png", "Visualiser", this::handleViewStudent);
        addButtonToColumn(colModifier, "/com/miniproject/images/modify-icon.png", "Modifier", this::handleUpdateStudent);
        addButtonToColumn(colSupprimer, "/com/miniproject/images/delete-icon.png", "Supprimer", this::handleDeleteStudent);
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
     * Checks if an Etudiant matches the search filter.
     *
     * @param etudiant         The Etudiant to check.
     * @param lowerCaseFilter  The search filter in lowercase.
     * @return True if the Etudiant matches the filter; otherwise, false.
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
     * Handles viewing enrolled modules for a student.
     *
     * @param etudiant The selected Etudiant.
     */
    private void handleViewEnrolledModules(Etudiant etudiant) {
        if (etudiant == null) {
            showAlert(Alert.AlertType.WARNING, "No Student Selected", "Please select a student to view their enrolled modules.");
            return;
        }

        List<Module> enrolledModules = inscriptionDAO.getModulesByStudentId(etudiant.getId());

        if (enrolledModules.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Modules", "This student is not enrolled in any modules.");
        } else {
            showModulesPopup(enrolledModules, etudiant);
        }
    }

    /**
     * Displays a popup with the enrolled modules for a student.
     *
     * @param modules   The list of enrolled modules.
     * @param etudiant  The selected Etudiant.
     */
    private void showModulesPopup(List<Module> modules, Etudiant etudiant) {
        StringBuilder moduleDetails = new StringBuilder();
        moduleDetails.append("Modules for ")
                .append(etudiant.getNom())
                .append(" ")
                .append(etudiant.getPrenom())
                .append(":\n\n");

        for (Module module : modules) {
            moduleDetails.append("- ")
                    .append(defaultIfNull(module.getNomModule(), "Unknown"))
                    .append(" (Code: ")
                    .append(defaultIfNull(module.getCodeModule(), "Unknown"))
                    .append(", Professor: ")
                    .append(defaultIfNull(module.getProfessorFullName(), "Unknown"))
                    .append(")\n");
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enrolled Modules");
        alert.setHeaderText("Enrolled Modules for " + etudiant.getNom() + " " + etudiant.getPrenom());
        alert.setContentText(moduleDetails.toString());
        alert.showAndWait();
    }

    /**
     * Returns the default value if the original value is null.
     *
     * @param value         The original value.
     * @param defaultValue  The default value to return if original is null.
     * @return The original value or the default value if original is null.
     */
    private String defaultIfNull(String value, String defaultValue) {
        return (value != null) ? value : defaultValue;
    }

    /**
     * Opens a popup window for adding, editing, or viewing a student.
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
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.setMinWidth(500);
            popupStage.setMinHeight(650);
            popupStage.centerOnScreen();
            popupStage.showAndWait();

            loadEtudiants(); // Refresh data after popup closes

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to open the window.");
        }
    }

    /**
     * Handles adding a new student.
     */
    @FXML
    private void handleAddStudent() {
        openPopup("/com/miniproject/view/Etudiant/add-student.fxml", "Add Student", controller -> {
            if (controller instanceof AddStudentController addController) {
                addController.setEtudiantController(this);
            }
        });
    }
    /**
     * Handles updating an existing student.
     *
     * @param etudiant The selected Etudiant to update.
     */
    private void handleUpdateStudent(Etudiant etudiant) {
        if (etudiant == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to modify.");
            return;
        }

        openPopup("/com/miniproject/view/Etudiant/edit-student.fxml", "Edit Student", controller -> {
            if (controller instanceof EditStudentController editController) {
                editController.setEtudiantController(this);
                editController.setEtudiant(etudiant);
            }
        });
    }

    /**
     * Handles viewing the details of a student.
     *
     * @param etudiant The selected Etudiant to view.
     */
    private void handleViewStudent(Etudiant etudiant) {
        if (etudiant == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to view.");
            return;
        }

        openPopup("/com/miniproject/view/Etudiant/view-student.fxml", "View Student", controller -> {
            if (controller instanceof ViewStudentController viewController) {
                viewController.setEtudiant(etudiant);
            }
        });
    }

    /**
     * Handles deleting a selected student.
     *
     * @param etudiant The selected Etudiant to delete.
     */
    private void handleDeleteStudent(Etudiant etudiant) {
        if (etudiant == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a student to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Delete Student");
        confirmation.setContentText("Are you sure you want to delete this student?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            etudiantDAO.delete(etudiant.getId());
            loadEtudiants();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Student deleted successfully!");
        }
    }


    @FXML
    private void handleExportStudents() {
        // Open a FileChooser to select the destination file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les Étudiants en CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Show save dialog
        File file = fileChooser.showSaveDialog(etudiantTable.getScene().getWindow());
        if (file != null) {
            exportToCSV(file);
        }
    }
    @FXML
    private void handleExportToPDF() {
        // Open a FileChooser to select the destination file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exporter les Étudiants en PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(etudiantTable.getScene().getWindow());
        if (file != null) {
            exportToPDF(file);
        }
    }

    /**
     * Exports the list of students to a CSV file using standard Java I/O.
     *
     * @param file The destination file.
     */
    private void exportToCSV(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write CSV header
            writer.write("ID,Matricule,Nom,Prenom,DateNaissance,Email,Promotion");
            writer.newLine();

            // Write student data
            for (Etudiant etudiant : etudiantList) {
                // Escape commas, quotes, and newlines in data fields
                String id = String.valueOf(etudiant.getId());
                String matricule = escapeSpecialCharacters(etudiant.getMatricule());
                String nom = escapeSpecialCharacters(etudiant.getNom());
                String prenom = escapeSpecialCharacters(etudiant.getPrenom());
                String dateNaissance = escapeSpecialCharacters(etudiant.getDateNaissance());
                String email = escapeSpecialCharacters(etudiant.getEmail());
                String promotion = escapeSpecialCharacters(etudiant.getPromotion());

                String line = String.format("%s,%s,%s,%s,%s,%s,%s",
                        id, matricule, nom, prenom, dateNaissance, email, promotion);
                writer.write(line);
                writer.newLine();
            }

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les étudiants ont été exportés en CSV avec succès!");

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
            document.add(new Paragraph("Liste des Étudiants").setBold().setFontSize(16).setMarginLeft(50).setMarginBottom(10).setMarginTop(10).setFont(font));

            // Create a table with appropriate column count
            Table table = new Table(new float[]{1, 3, 3, 3, 3, 4, 2});
            table.setWidth(com.itextpdf.layout.properties.UnitValue.createPercentValue(100));

            // Add table headers
            table.addHeaderCell("ID").setFont(font);
            table.addHeaderCell("Matricule").setFont(font);
            table.addHeaderCell("Nom").setFont(font);
            table.addHeaderCell("Prénom").setFont(font);
            table.addHeaderCell("Date de Naissance").setFont(font);
            table.addHeaderCell("Email").setFont(font);
            table.addHeaderCell("Promotion").setFont(font);

            // Add student data to the table
            for (Etudiant etudiant : etudiantList) {
                table.addCell(String.valueOf(etudiant.getId())).setFont(font);
                table.addCell(etudiant.getMatricule()).setFont(font);
                table.addCell(etudiant.getNom()).setFont(font);
                table.addCell(etudiant.getPrenom()).setFont(font);
                table.addCell(etudiant.getDateNaissance()).setFont(font);
                table.addCell(etudiant.getEmail()).setFont(font);
                table.addCell(etudiant.getPromotion()).setFont(font);
            }

            // Add the table to the document
            document.add(table);

            // Close the document
            document.close();

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Les étudiants ont été exportés en PDF avec succès!");
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

}
