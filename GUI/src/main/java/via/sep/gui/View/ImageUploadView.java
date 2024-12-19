package via.sep.gui.View;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import via.sep.gui.Model.domain.ImageViewModel;
import via.sep.gui.ViewModel.ImageUploadViewModel;

import java.io.File;
import java.util.List;

public class ImageUploadView {
    @FXML
    private Button uploadButton;
    @FXML
    private ScrollPane imageScrollPane;
    @FXML
    private FlowPane imageContainer;
    @FXML
    Label statusLabel;

    private ImageUploadViewModel viewModel;

    @FXML
    private void initialize() {
        imageContainer = new FlowPane();
        imageContainer.setHgap(10);
        imageContainer.setVgap(10);
        imageContainer.setPadding(new Insets(10));

        imageScrollPane.setContent(imageContainer);
        imageScrollPane.setFitToWidth(true);

        uploadButton.setOnAction(event -> handleImageUpload());
    }

    public void setViewModel(ImageUploadViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.getImages().addListener((ListChangeListener<ImageViewModel>) change -> updateImageDisplay());
        statusLabel.textProperty().bind(viewModel.statusMessageProperty());

        updateImageDisplay();
    }

    private void handleImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png")
        );

        List<File> files = fileChooser.showOpenMultipleDialog(uploadButton.getScene().getWindow());

        if (files != null) {
            viewModel.addImages(files);
        }
    }

    private void updateImageDisplay() {
        imageContainer.getChildren().clear();

        for (ImageViewModel image : viewModel.getImages()) {
            try {
                String imageUrl = image.getPreviewUrl();
                if (imageUrl != null) {
                    Image fxImage = new Image(imageUrl);
                    ImageView imageView = new ImageView(fxImage);
                    imageView.setFitHeight(150);
                    imageView.setFitWidth(150);
                    imageView.setPreserveRatio(true);

                    Button deleteButton = new Button("Remove");
                    deleteButton.setOnAction(e -> {
                        if (image.getId() != null) {
                            viewModel.deleteImage(image.getId());
                        } else {
                            viewModel.removePreviewImage(image);
                        }
                    });

                    VBox imageBox = new VBox(10);
                    imageBox.setAlignment(Pos.CENTER);
                    imageBox.getChildren().addAll(imageView, deleteButton);
                    imageContainer.getChildren().add(imageBox);
                }
            } catch (Exception e) {
                System.err.println("Error displaying image: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
