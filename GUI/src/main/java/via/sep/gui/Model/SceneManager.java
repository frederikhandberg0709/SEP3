package via.sep.gui.Model;


import com.google.gson.Gson;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.View.CreateView;
import via.sep.gui.View.EditView;
import via.sep.gui.View.LoginView;
import via.sep.gui.View.RegisterView;
import via.sep.gui.ViewModel.LoginViewModel;
import via.sep.gui.ViewModel.RegisterViewModel;
import via.sep.gui.ViewModel.DashboardViewModel;
import via.sep.gui.ViewModel.CreateViewModel;
import via.sep.gui.ViewModel.EditViewModel;
import via.sep.gui.View.DashBoardView;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;
    private static ServerConnection serverConnection;
    private static Gson gson;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void setServerConnection(ServerConnection connection) {
        serverConnection = connection;
    }

    public static void setGson(Gson gsonInstance) {
        gson = gson;
    }

    public static void showRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/Register.fxml"));
            Parent root = loader.load();

            RegisterView registerView = loader.getController();
            RegisterViewModel registerViewModel = new RegisterViewModel(serverConnection, gson);
            registerView.setViewModel(registerViewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/Login.fxml"));
            Parent root = loader.load();


            LoginView loginView = loader.getController();
            LoginViewModel loginViewModel = new LoginViewModel(serverConnection, gson);
            loginView.setViewModel(loginViewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/Dashboard.fxml"));
            Parent root = loader.load();


            DashBoardView dashboardView = loader.getController();
            PropertyService propertyService = new PropertyService(serverConnection, gson);
            DashboardViewModel dashboardViewModel = new DashboardViewModel(propertyService);
            dashboardView.setViewModel(dashboardViewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showCreateProperty() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/Create.fxml"));
            Parent root = loader.load();

            CreateView createView = loader.getController();
            PropertyService propertyService = new PropertyService(serverConnection, gson);
            CreateViewModel createViewModel = new CreateViewModel(propertyService);
            createView.setViewModel(createViewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showEditProperty(Property property) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/Edit.fxml"));
            Parent root = loader.load();

            EditView editView = loader.getController();
            PropertyService propertyService = new PropertyService(serverConnection, gson);
            EditViewModel editViewModel = new EditViewModel(propertyService);
            editViewModel.loadProperty(property);
            editView.setViewModel(editViewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showBookingList() {
        System.out.println("Booking list view not implemented yet");
    }
}
