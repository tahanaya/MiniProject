module com.miniproject.miniproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires static lombok;

    opens com.miniproject to javafx.fxml;
    exports com.miniproject;
    exports com.miniproject.CONTROLLER;
    opens com.miniproject.CONTROLLER to javafx.fxml;

    // Export the TEST package specifically to javafx.graphics
    opens com.miniproject.TEST to javafx.graphics;


    // Export packages if other modules need to access them
    exports com.miniproject.ENTITY;

    // Open the ENTITY package to javafx.base for reflection
    opens com.miniproject.ENTITY to javafx.base, javafx.fxml;
    exports com.miniproject.CONTROLLER.ETUDIANT;
    opens com.miniproject.CONTROLLER.ETUDIANT to javafx.fxml;
    exports com.miniproject.CONTROLLER.DASHBOARD;
    opens com.miniproject.CONTROLLER.DASHBOARD to javafx.fxml;


}