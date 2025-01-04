package com.miniproject.CONTROLLER.TableauDeBord;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import com.miniproject.DAO.EtudiantDAOImpl;
import com.miniproject.DAO.ModuleDAOImpl;
import com.miniproject.DAO.ProfesseurDAOImpl;

import java.sql.SQLException;

public class TableauDeBordController {

    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalProfessorsLabel;
    @FXML
    private Label totalModulesLabel;
    @FXML
    private BarChart<String, Number> mostFollowedModulesChart;
    @FXML
    private PieChart professorsChart;

    private EtudiantDAOImpl etudiantDAO = new EtudiantDAOImpl();
    private ModuleDAOImpl moduleDAO = new ModuleDAOImpl();
    private ProfesseurDAOImpl professeurDAO = new ProfesseurDAOImpl();

    @FXML
    public void initialize() {
        try {
            totalStudentsLabel.setText(String.valueOf(etudiantDAO.countStudents()));
            totalProfessorsLabel.setText(String.valueOf(professeurDAO.countProfessors()));
            totalModulesLabel.setText(String.valueOf(moduleDAO.countModules()));
            updateCharts();
        } catch (SQLException e) {
            // Log the exception and/or show an error message to the user
            e.printStackTrace();
        }
    }

    private void updateCharts() throws SQLException {
        updateModulesChart();
        updateProfessorsChart();
    }

//    private void updateStatistics() {
//        totalStudentsLabel.setText(String.valueOf(etudiantDAO.countStudents()));
//        totalProfessorsLabel.setText(String.valueOf(professeurDAO.countProfessors()));
//        totalModulesLabel.setText(String.valueOf(moduleDAO.countModules()));
//
//        updateModulesChart();
//        updateProfessorsChart();
//    }

    private void updateModulesChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        moduleDAO.getMostFollowedModules().forEach((name, count) -> {
            series.getData().add(new XYChart.Data<>(name, count));
        });
        mostFollowedModulesChart.getData().clear();
        mostFollowedModulesChart.getData().add(series);
    }

    private void updateProfessorsChart() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        professeurDAO.getProfessorsWithMostModules().forEach((name, count) -> {
            data.add(new PieChart.Data(name, count));
        });
        professorsChart.setData(data);

    }
}
