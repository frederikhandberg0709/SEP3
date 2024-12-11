package via.sep.gui;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.View.*;
import via.sep.gui.ViewModel.*;


public class Main extends Application {
    private ServerConnection serverConnection;
    private Gson gson;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        SceneManager.setPrimaryStage(primaryStage);
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/via.sep.gui/View/DashBoard.fxml"));
//        Pane root = loader.load();

        serverConnection = new ServerConnection();
        gson = new Gson();

        //DashBoardView dashboardView = loader.getController();

        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.setServerConnection(serverConnection);
        SceneManager.setGson(gson);

//        Scene scene = new Scene(root);
//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Property Management");
//        primaryStage.show();

        SceneManager.showLogin();

        primaryStage.setTitle("Property Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
