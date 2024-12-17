package via.sep.gui.Model;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import via.sep.gui.Model.domain.Property;
import via.sep.gui.Server.ServerConnection;
import via.sep.gui.View.*;
import via.sep.gui.ViewModel.*;

import java.io.IOException;
import java.lang.reflect.Modifier;

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
        gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .setPrettyPrinting()
                .serializeNulls()
                .disableInnerClassSerialization()
                .setLenient()
                .create();
    }

    public static ServerConnection getServerConnection() {
        return serverConnection;
    }

    public static Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
                    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                    .setPrettyPrinting()
                    .serializeNulls()
                    .disableInnerClassSerialization()
                    .setLenient()
                    .create();
        }
        return gson;
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

            DashboardView dashboardView = loader.getController();
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
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/CreateProperty.fxml"));
            Parent root = loader.load();

            CreatePropertyView createView = loader.getController();
            PropertyService propertyService = new PropertyService(serverConnection, gson);
            ImageService imageService = new ImageService(serverConnection, gson);
            CreatePropertyViewModel createViewModel = new CreatePropertyViewModel(propertyService, imageService);

            createView.setViewModel(createViewModel);
            createView.setImageService(imageService);
            createView.setImageUploadViewModel(createViewModel.getImageUploadViewModel());

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showEditProperty(Property property) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/EditProperty.fxml"));
            Parent root = loader.load();

            EditPropertyView editView = loader.getController();
            PropertyService propertyService = new PropertyService(serverConnection, gson);
            ImageService imageService = new ImageService(serverConnection, gson);

            EditPropertyViewModel editViewModel = new EditPropertyViewModel(propertyService, imageService);
            editView.setViewModel(editViewModel);
            editView.setImageService(imageService);
            editView.setImageUploadViewModel(editViewModel.getImageUploadViewModel());

            editViewModel.loadProperty(property);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showImageUploadForProperty(ImageUploadViewModel imageUploadViewModel) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/ImageUpload.fxml"));
            Parent root = loader.load();

            ImageUploadView imageUploadView = loader.getController();

            imageUploadView.setViewModel(imageUploadViewModel);

            Stage imageStage = new Stage();
            imageStage.initModality(Modality.APPLICATION_MODAL);
            imageStage.initOwner(primaryStage);
            imageStage.setScene(new Scene(root));
            imageStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showBookingList() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/BookingList.fxml"));
            Parent root = loader.load();

            BookingListView bookingListView = loader.getController();
            BookingService bookingService = new BookingService(serverConnection, gson);
            BookingListViewModel bookingListViewModel = new BookingListViewModel(bookingService);
            bookingListView.setViewModel(bookingListViewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void showAgentList() {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/via.sep.gui/View/AgentList.fxml"));
            Parent root = loader.load();

            AgentListView agentListView = loader.getController();
            AgentService agentService = new AgentService(serverConnection, gson);
            AgentListViewModel agentListViewModel = new AgentListViewModel(agentService);
            agentListView.setViewModel(agentListViewModel);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
