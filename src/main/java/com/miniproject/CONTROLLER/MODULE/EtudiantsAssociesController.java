package com.miniproject.CONTROLLER.MODULE;

import com.miniproject.DAO.InscriptionDAOImpl;
import com.miniproject.ENTITY.Etudiant;
import com.miniproject.ENTITY.Module;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class EtudiantsAssociesController {
    @FXML private TableView<Etudiant> studentsTable;
    @FXML private TableColumn<Etudiant, Integer> idColumn;
    @FXML private TableColumn<Etudiant, String> nameColumn;
    @FXML private TableColumn<Etudiant, String> prenomColumn;

    private final InscriptionDAOImpl inscriptionDAO = new InscriptionDAOImpl();

    private Module module;

    public void setModule(Module module) {
        this.module = module;

        // Load associated students
        List<Etudiant> students = inscriptionDAO.getStudentsByModuleId(module.getId());
        ObservableList<Etudiant> studentList = FXCollections.observableArrayList(students);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));

        studentsTable.setItems(studentList);
    }
}
