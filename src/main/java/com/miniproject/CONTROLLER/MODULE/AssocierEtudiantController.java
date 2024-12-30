package com.miniproject.CONTROLLER.MODULE;

import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.InscriptionDAOImpl;
import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.List;

public class AssocierEtudiantController {
    @FXML
    private ComboBox<Etudiant> studentComboBox;
    @FXML
    private Label statusLabel;

    private final EtudiantDAOImpl etudiantDAO = new EtudiantDAOImpl();
    private final InscriptionDAOImpl inscriptionDAO = new InscriptionDAOImpl();

    private Module module;

    public void setModule(Module module) {
        this.module = module;
        loadStudents();
    }

    private void loadStudents() {
        // Load students into ComboBox
        List<Etudiant> students = etudiantDAO.findAll();
        ObservableList<Etudiant> studentList = FXCollections.observableArrayList(students);
        studentComboBox.setItems(studentList);
        // Customize display in ComboBox
        studentComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Etudiant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMatricule() + " - " + item.getNom() + " " + item.getPrenom());
                }
            }
        });
        // Set the display for the selected item in ComboBox
        studentComboBox.setButtonCell(new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Etudiant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getMatricule() + " - " + item.getNom() + " " + item.getPrenom());
                }
            }
        });
    }


    @FXML
    private void associateStudent() {
        Etudiant selectedStudent = studentComboBox.getValue();
        if (selectedStudent != null) {
            if (inscriptionDAO.isStudentInModule(module.getId(), selectedStudent.getId())) {
                statusLabel.setText("L'étudiant est déjà associé à ce module.");
                statusLabel.setStyle("-fx-text-fill: orange;"); // Orange for warnings
            } else {
                try {
                    inscriptionDAO.addStudentToModule(module.getId(), selectedStudent.getId());
                    statusLabel.setText("Étudiant associé avec succès !");
                    statusLabel.setStyle("-fx-text-fill: green;"); // Green for success
                } catch (Exception e) {
                    statusLabel.setText("Erreur lors de l'association : " + e.getMessage());
                    statusLabel.setStyle("-fx-text-fill: red;"); // Red for errors
                }
            }
        } else {
            statusLabel.setText("Veuillez sélectionner un étudiant.");
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }
}