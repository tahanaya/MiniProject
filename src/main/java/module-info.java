module com.miniproject.miniproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires static lombok;

    opens com.miniproject to javafx.fxml;
    exports com.miniproject;
}