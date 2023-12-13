module com.example.demoui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.example.demoui to javafx.fxml;
    exports com.example.demoui;
}