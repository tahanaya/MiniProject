module com.miniproject.miniproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires static lombok;

    // General exports and opens
    opens com.miniproject to javafx.fxml;
    exports com.miniproject;

    exports com.miniproject.CONTROLLER;
    opens com.miniproject.CONTROLLER to javafx.fxml;

    // Specific exports and opens
    opens com.miniproject.TEST to javafx.graphics;

    exports com.miniproject.ENTITY;
    opens com.miniproject.ENTITY to javafx.base, javafx.fxml;

    exports com.miniproject.CONTROLLER.ETUDIANT;
    opens com.miniproject.CONTROLLER.ETUDIANT to javafx.fxml;

    exports com.miniproject.CONTROLLER.DASHBOARD;
    opens com.miniproject.CONTROLLER.DASHBOARD to javafx.fxml;

    // Add these lines for the PROFESSEUR package
    exports com.miniproject.CONTROLLER.PROFESSEUR;
    opens com.miniproject.CONTROLLER.PROFESSEUR to javafx.fxml;
    // Add these lines for the Module package
    exports com.miniproject.CONTROLLER.MODULE;
    opens com.miniproject.CONTROLLER.MODULE to javafx.fxml;
    exports com.miniproject.DAO;
    opens com.miniproject.DAO to javafx.fxml;





}
