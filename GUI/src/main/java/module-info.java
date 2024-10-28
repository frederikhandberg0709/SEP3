module via.sep.gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens via.sep.gui to javafx.fxml;
    exports via.sep.gui;
}