module via.sep.gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens via.sep.gui.View to javafx.fxml;
    exports via.sep.gui;
}