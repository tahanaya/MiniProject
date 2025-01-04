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

    // FXML-injected TableView and TableColumns
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


    // DAO for Etudiant operations
    private final GenericDAO<Etudiant> etudiantDAO = new EtudiantDAOImpl();

    // ObservableList to hold Etudiant data for the TableView
    private final ObservableList<Etudiant> etudiantList = FXCollections.observableArrayList();

    private final InscriptionDAOImpl inscriptionDAO = new InscriptionDAOImpl();
    private final ModuleDAOImpl moduleDAO = new ModuleDAOImpl();

    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        System.out.println("EtudiantController initialized."); // Debugging statement

        // Initialize table columns with PropertyValueFactory
        setupTableColumns();

        // Load student data from the DAO
        loadEtudiants();

        // Add buttons to specific columns
        addButtonToColumn(colModulesAssocies, "/com/miniproject/images/modules-enrolled.png", "Modules Associés", this::handleViewEnrolledModules);
        addButtonToColumn(colVisualiser, "/com/miniproject/images/visualize-icon.png", "Visualiser", this::handleViewStudent);
        addButtonToColumn(colModifier, "/com/miniproject/images/modify-icon.png", "Modifier", this::handleUpdateStudent);
        addButtonToColumn(colSupprimer, "/com/miniproject/images/delete-icon.png", "Supprimer", this::handleDeleteStudent);

        // Création d'une liste filtrée basée sur la liste observable d'étudiants
        FilteredList<Etudiant> filteredList = new FilteredList<>(etudiantList, p -> true);

        // Ajout d'un listener sur le champ de recherche
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(etudiant -> {
                // Si le champ de recherche est vide, afficher tous les étudiants
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Comparer chaque attribut de l'étudiant avec la valeur de recherche
                return (etudiant.getMatricule() != null && etudiant.getMatricule().toLowerCase().contains(lowerCaseFilter)) ||
                        (etudiant.getNom() != null && etudiant.getNom().toLowerCase().contains(lowerCaseFilter)) ||
                        (etudiant.getPrenom() != null && etudiant.getPrenom().toLowerCase().contains(lowerCaseFilter)) ||
                        (etudiant.getDateNaissance() != null && etudiant.getDateNaissance().toLowerCase().contains(lowerCaseFilter)) ||
                        (etudiant.getEmail() != null && etudiant.getEmail().toLowerCase().contains(lowerCaseFilter)) ||
                        (etudiant.getPromotion() != null && etudiant.getPromotion().toLowerCase().contains(lowerCaseFilter));
            });
        });

        // Bind the data to the TableView
        etudiantTable.setItems(filteredList);
    }

    //Pour afficher pour chaque étudiant les bouton d'afficages de ses modules
    private void addButtonToColumn(TableColumn<Etudiant, Void> column, String iconPath, String tooltipText, Consumer<Etudiant> action) {
        column.setCellFactory(param -> new TableCell<>() {
            private final Button button = new Button();

            {
                // Set icon for the button
                ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
                icon.setFitHeight(20); // Adjust icon height
                icon.setFitWidth(20);  // Adjust icon width
                button.setGraphic(icon);

                // Style button
                button.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                Tooltip.install(button, new Tooltip(tooltipText));

                // Attach action
                button.setOnAction(event -> {
                    Etudiant etudiant = getTableView().getItems().get(getIndex());
                    action.accept(etudiant);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });
    }

    //    private void addActionButtonsToTable() {
//        colActions.setCellFactory(param -> new TableCell<>() {
//            private final Button viewModulesButton = new Button("View Modules");
//
//            {
//                // Style the button (optional)
//                viewModulesButton.setStyle("-fx-background-color: #a7b2f9; -fx-text-fill: #11161f; -fx-max-width: 150 ; -fx-max-height: 1;");
//
//                // Attach an action listener to the button
//                viewModulesButton.setOnAction(event -> {
//                    Etudiant etudiant = getTableView().getItems().get(getIndex());
//                    handleViewEnrolledModules(etudiant);
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    setGraphic(viewModulesButton);
//                }
//            }
//        });
//    }
    private void handleViewEnrolledModules(Etudiant etudiant) {
        if (etudiant == null) {
            showAlert(Alert.AlertType.WARNING, "No Student Selected", "Please select a student to view their enrolled modules.");
            return;
        }

        // Fetch enrolled modules for the student
        List<Module> enrolledModules = new InscriptionDAOImpl().getModulesByStudentId(etudiant.getId());


        // Display the enrolled modules
        if (enrolledModules.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Modules", "This student is not enrolled in any modules.");
        } else {
            showModulesPopup(enrolledModules, etudiant);
        }
    }

    private void showModulesPopup(List<Module> modules, Etudiant etudiant) {
        StringBuilder moduleDetails = new StringBuilder();
        moduleDetails.append("Modules for ")
                .append(etudiant.getNom())
                .append(" ")
                .append(etudiant.getPrenom())
                .append(":\n\n");

        // Validation des modules
        if (modules == null || modules.isEmpty()) {
            moduleDetails.append("No modules are enrolled.\n");
        } else {
            for (Module module : modules) {
                moduleDetails.append("- ")
                        .append(module.getNomModule() != null ? module.getNomModule() : "Unknown")
                        .append(" (Code: ")
                        .append(module.getCodeModule() != null ? module.getCodeModule() : "Unknown")
                        .append(", Professor: ")
                        .append(module.getProfessorFullName() != null ? module.getProfessorFullName() : "Unknown")
                        .append(")\n");
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Enrolled Modules");
        alert.setHeaderText("Enrolled Modules for " + etudiant.getNom() + " " + etudiant.getPrenom());
        alert.setContentText(moduleDetails.toString());
        alert.showAndWait();
    }


    //Pour le bouton de Recherche
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
     * Handles the addition of a new student.
     */
    @FXML
    private void handleAddStudent() {
        openAddStudentPopup();
    }

    /**
     * Handles updating an existing student.
     */
    @FXML
    private void handleUpdateStudent(Etudiant selected ) {
        //Etudiant selected = etudiantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            openEditStudentPopup(selected);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à modifier.");
        }
    }

    /**
     * Handles deleting a selected student.
     */
    @FXML
    private void handleDeleteStudent(Etudiant selected) {
        //Etudiant selected = etudiantTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Confirm deletion
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation");
            confirmation.setHeaderText("Supprimer Étudiant");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cet étudiant?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete from database
                etudiantDAO.delete(selected.getId());
                System.out.println("Deleted student with ID: " + selected.getId()); // Debugging statement

                // Refresh data
                loadEtudiants();

                showAlert(Alert.AlertType.INFORMATION, "Succès", "Étudiant supprimé avec succès!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à supprimer.");
        }
    }

    /**
     * Handles viewing the selected student's details.
     */
    @FXML
    private void handleViewStudent(Etudiant etudiant) {
        //Etudiant selected = etudiantTable.getSelectionModel().getSelectedItem();
        if (etudiant != null) {
            openViewStudentPopup(etudiant);
        } else {
            showAlert(Alert.AlertType.WARNING, "Aucune Sélection", "Veuillez sélectionner un étudiant à visualiser.");
        }
    }

    /**
     * Handles exporting the list of students to a CSV file.
     */
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
     * Opens the Add Student popup window.
     */
    private void openAddStudentPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/add-student.fxml"));
            Parent root = loader.load();

            // Get the controller of the Add Student popup and pass this controller
            AddStudentController addStudentController = loader.getController();
            addStudentController.setEtudiantController(this);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 350));
            popupStage.setTitle("Ajouter un étudiant");
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

            // Refresh data after adding a student
            loadEtudiants();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre d'ajout.");
        }
    }

    /**
     * Opens the Edit Student popup window for the selected student.
     *
     * @param etudiant The selected Etudiant to edit.
     */
    private void openEditStudentPopup(Etudiant etudiant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/edit-student.fxml"));
            Parent root = loader.load();

            // Get the controller of the Edit Student popup and pass this controller and the selected student
            EditStudentController editStudentController = loader.getController();
            editStudentController.setEtudiantController(this);
            editStudentController.setEtudiant(etudiant);

            Stage popupStage = new Stage();
            popupStage.setScene(new Scene(root, 400, 350));
            popupStage.setTitle("Modifier un étudiant");
            popupStage.initOwner(etudiantTable.getScene().getWindow());
            popupStage.showAndWait();  // Wait for popup to close

            // Refresh data after updating a student
            loadEtudiants();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible d'ouvrir la fenêtre de modification.");
        }
    }

    /**
     * Opens the View Student popup window for the selected student.
     *
     * @param etudiant The selected Etudiant to view.
     */
    private void openViewStudentPopup(Etudiant etudiant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/view-student.fxml"));
            Parent root = loader.load();

            // Get the controller of the View Student popup and pass the selected student
            ViewStudentController viewStudentController = loader.getController();
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

    /**
     * Displays an alert dialog to the user.
     *
     * @param type    The type of alert.
     * @param title   The title of the dialog.
     * @param message The content message.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
