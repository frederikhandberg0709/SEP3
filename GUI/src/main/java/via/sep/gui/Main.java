package via.sep.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import via.sep.gui.View.CreateView;
import via.sep.gui.View.EditView;
import via.sep.gui.View.LoginView;
import via.sep.gui.View.RegisterView;
import via.sep.gui.ViewModel.CreateViewModel;
import via.sep.gui.ViewModel.EditViewModel;
import via.sep.gui.ViewModel.LoginViewModel;
import via.sep.gui.ViewModel.RegisterViewModel;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/via.sep.gui/View/Edit.fxml"));
        Pane root = loader.load();

        // Get the controller instance and set the ViewModel
        EditView editView = loader.getController();
        editView.setViewModel(new EditViewModel());


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Property Management");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);


    }
}