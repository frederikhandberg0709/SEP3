package via.sep.gui;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.stage.Stage;
import via.sep.gui.ViewModel.SceneManager;
import via.sep.gui.Server.ServerConnection;

public class Main extends Application {
    private ServerConnection serverConnection;
    private Gson gson;

    @Override
    public void start(Stage primaryStage) throws Exception {

        serverConnection = new ServerConnection();
        gson = new Gson();

        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.setServerConnection(serverConnection);
        SceneManager.setGson(gson);

        SceneManager.showLogin();

        primaryStage.setTitle("Property Management System");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
