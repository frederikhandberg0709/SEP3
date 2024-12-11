module via.sep.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.postgresql.jdbc;
    requires com.google.gson;
    requires java.net.http;

    // Export the main package for application entry point
    exports via.sep.gui;

    // Open specific packages for FXML access
    opens via.sep.gui.View to javafx.fxml;
    opens via.sep.gui.Model.dto to javafx.fxml, com.google.gson;
    opens via.sep.gui.Model.domain to javafx.fxml, com.google.gson, javafx.base;
}
