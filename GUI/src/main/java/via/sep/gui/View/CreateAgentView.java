package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import via.sep.gui.Model.SceneManager;
import via.sep.gui.ViewModel.CreateAgentViewModel;

public class CreateAgentView {
    @FXML
    private TextField nameField;
    @FXML private TextField contactInfoField;
    @FXML private Button createButton;
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;

    private CreateAgentViewModel viewModel;

    @FXML
    private void initialize() {
        createButton.setOnAction(event -> handleCreate());
        cancelButton.setOnAction(event -> handleCancel());
    }

    private void handleCreate() {
        if (viewModel.createAgent()) {
            SceneManager.showDashboard();
        }
    }

    private void handleCancel() {
        SceneManager.showDashboard();
    }

    public void setViewModel(CreateAgentViewModel viewModel) {
        this.viewModel = viewModel;

        nameField.textProperty().bindBidirectional(viewModel.nameProperty());
        contactInfoField.textProperty().bindBidirectional(viewModel.contactInfoProperty());
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
    }
}
