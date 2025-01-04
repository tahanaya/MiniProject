package com.miniproject;

import com.miniproject.CONTROLLER.LandingPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the landing page first
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/miniproject/view/LandingPageView.fxml"));
        Scene scene = new Scene(loader.load());

        // Apply the global CSS
        scene.getStylesheets().add(getClass().getResource("/com/miniproject/css/styles.css").toExternalForm());

        // Pass the primary stage to the landing page controller
        LandingPageController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome to MiniProject");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
