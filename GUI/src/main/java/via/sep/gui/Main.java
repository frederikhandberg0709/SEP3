package via.sep.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.View.*;
import via.sep.gui.ViewModel.*;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.setPrimaryStage(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/via.sep.gui/View/DashBoard.fxml"));
        Pane root = loader.load();

        // Get the controller instance and set the ViewModel
        DashBoardView dashboardView = loader.getController();
        dashboardView.setViewModel(new DashboardViewModel());


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Property Management");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);


    }
}