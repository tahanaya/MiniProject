module com.miniproject.miniproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.miniproject.miniproject to javafx.fxml;
    exports com.miniproject.miniproject;
}