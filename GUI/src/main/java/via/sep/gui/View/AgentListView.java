package via.sep.gui.View;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import via.sep.gui.Model.domain.Agent;
import via.sep.gui.ViewModel.AgentListViewModel;

public class AgentListView {
    @FXML private TableView<Agent> agentTable;
    @FXML private TableColumn<Agent, String> nameColumn;
    @FXML private TableColumn<Agent, String> contactInfoColumn;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button closeButton;

    private AgentListViewModel viewModel;

    @FXML
    private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        contactInfoColumn.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));

        // Disable remove button if no selection
        removeButton.disableProperty().bind(agentTable.getSelectionModel().selectedItemProperty().isNull());
    }

    public void setViewModel(AgentListViewModel viewModel) {
        this.viewModel = viewModel;
        agentTable.setItems(viewModel.getAgents());
    }

    @FXML
    private void handleAddAgent() {
        Dialog<Agent> dialog = new Dialog<>();
        dialog.setTitle("Add Agent");
        dialog.setHeaderText("Enter agent details");

        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        TextField contactInfoField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Contact Info:"), 0, 1);
        grid.add(contactInfoField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                if (nameField.getText().isEmpty() || contactInfoField.getText().isEmpty()) {
                    showAlert("Error", "Please fill in all fields");
                    return null;
                }
                return viewModel.createAgent(nameField.getText(), contactInfoField.getText());
            }
            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleRemoveAgent() {
        Agent selectedAgent = agentTable.getSelectionModel().getSelectedItem();
        if (selectedAgent != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Agent");
            alert.setContentText("Are you sure you want to delete this agent?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    viewModel.deleteAgent(selectedAgent.getAgentId());
                }
            });
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleClose() {
        viewModel.closeAgentList();
    }
}
