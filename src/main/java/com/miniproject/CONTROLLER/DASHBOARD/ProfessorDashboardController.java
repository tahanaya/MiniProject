package com.miniproject.CONTROLLER.DASHBOARD;

import com.miniproject.CONTROLLER.ETUDIANT.ProfessorViewStudentController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import java.io.IOException;

public class ProfessorDashboardController {

    @FXML
    private Button viewCoursesButton; // Button to view courses

    @FXML
    private Button manageAssignmentsButton; // Button to view students

    @FXML
    private AnchorPane contentPane;

    // Assuming you have a way to get the logged-in professor's ID
    // This could be through a session manager, authentication service, etc.
    private int loggedInProfessorId = 1; // Example ID; replace with actual logic

    /**
     * Handler for the "View Courses" button click.
     */
    @FXML
    private void handleViewCourses(ActionEvent event) {
        try {
            // Load the professor-view-courses.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/professor-view-courses.fxml"));
            Parent viewCoursesRoot = loader.load();

            // If you have a controller for courses, set any necessary data here

            // Set the loaded FXML into the contentPane
            contentPane.getChildren().setAll(viewCoursesRoot);
            AnchorPane.setTopAnchor(viewCoursesRoot, 0.0);
            AnchorPane.setBottomAnchor(viewCoursesRoot, 0.0);
            AnchorPane.setLeftAnchor(viewCoursesRoot, 0.0);
            AnchorPane.setRightAnchor(viewCoursesRoot, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, display an alert to inform the user of the error
        }
    }

    /**
     * Handler for the "View Students" button click.
     */
    @FXML
    private void handleViewStudents(ActionEvent event) {
        try {
            // Load the professor-view-students.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/Etudiant/professor-view-students.fxml"));
            Parent viewStudentsRoot = loader.load();

            // Get the controller and set the professor ID
            ProfessorViewStudentController controller = loader.getController();
            controller.setProfessorId(loggedInProfessorId);

            // Set the loaded FXML into the contentPane
            contentPane.getChildren().setAll(viewStudentsRoot);
            AnchorPane.setTopAnchor(viewStudentsRoot, 0.0);
            AnchorPane.setBottomAnchor(viewStudentsRoot, 0.0);
            AnchorPane.setLeftAnchor(viewStudentsRoot, 0.0);
            AnchorPane.setRightAnchor(viewStudentsRoot, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
            // Optionally, display an alert to inform the user of the error
        }
    }

    // Other handler methods for different dashboard functionalities...
}
