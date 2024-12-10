module via.sep.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.postgresql.jdbc;
    requires com.google.gson;

    // Export the main package for application entry point
    exports via.sep.gui;

    // Open specific packages for FXML access
    opens via.sep.gui.View to javafx.fxml;
    opens via.sep.gui.Model to javafx.fxml;
}
