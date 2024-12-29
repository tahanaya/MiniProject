package com.miniproject.CONTROLLER.ETUDIANT;

import com.miniproject.DAO.ProfesseurDAO;

import com.miniproject.ENTITY.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class ProfessorViewStudentController {





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
        private Label statusLabel;

        private ObservableList<Etudiant> studentList = FXCollections.observableArrayList();

        private ProfesseurDAO professorDAO = new ProfesseurDAO(); // Ensure DAO is properly instantiated

        private int professorId; // The ID of the currently logged-in professor

        /**
         * Initialize method called automatically after FXML is loaded.
         */
        @FXML
        public void initialize() {
            // Initialize the TableView columns
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colMatricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colDateNaissance.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colPromotion.setCellValueFactory(new PropertyValueFactory<>("promotion"));

            // Load students if professorId is already set
            if (professorId != 0) {
                loadEnrolledStudents();
            }
        }

        /**
         * Sets the professor ID. This should be called when loading this controller.
         *
         * @param professorId The ID of the professor.
         */
        public void setProfessorId(int professorId) {
            this.professorId = professorId;
            loadEnrolledStudents();
        }

        /**
         * Loads the list of students enrolled in the professor's courses.
         */
        private void loadEnrolledStudents() {
            try {
                List<Etudiant> students = professorDAO.getStudentsEnrolled(professorId);
                studentList.setAll(students);
                etudiantTable.setItems(studentList);
                statusLabel.setText("Étudiants chargés avec succès.");
            } catch (SQLException e) {
                e.printStackTrace();
                statusLabel.setText("Erreur lors du chargement des étudiants.");
            }
        }
    }


