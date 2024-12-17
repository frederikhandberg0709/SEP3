package via.sep.gui.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import via.sep.gui.Model.dto.ImageDTO;
import via.sep.gui.Server.ServerConnection;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class ImageService {
    private final ServerConnection serverConnection;
    private final Gson gson;
    private static final String IMAGE_ENDPOINT = "/images";

    public ImageService(ServerConnection serverConnection, Gson gson) {
        this.serverConnection = serverConnection;
        this.gson = gson;
    }

    public List<ImageDTO> getImagesForProperty(Long propertyId) {
        try {
            String response = serverConnection.sendGetRequest(IMAGE_ENDPOINT + "/property/" + propertyId);
            return gson.fromJson(response, new TypeToken<List<ImageDTO>>(){}.getType());
            //Type listType = new TypeToken<List<ImageDTO>>(){}.getType();
            //return gson.fromJson(response, listType);
        } catch (Exception e) {
            System.err.println("Error fetching images: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public ImageDTO uploadImage(Long propertyId, File imageFile) throws Exception {
        try {
            String boundary = "-------------" + System.currentTimeMillis();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] fileBytes = Files.readAllBytes(imageFile.toPath());


            baos.write(("--" + boundary + "\r\n").getBytes());

            baos.write(("Content-Disposition: form-data; name=\"image\"; filename=\"" + imageFile.getName() + "\"\r\n").getBytes());

            baos.write("Content-Type: application/octet-stream\r\n\r\n".getBytes());

            baos.write(fileBytes);
            baos.write("\r\n".getBytes());

            baos.write(("--" + boundary + "--\r\n").getBytes());

            String response = serverConnection.sendPostRequestMultipart(
                    IMAGE_ENDPOINT + "/property/" + propertyId,
                    baos.toByteArray(),
                    boundary
            );

            return gson.fromJson(response, ImageDTO.class);
        } catch (Exception e) {
            throw new Exception("Failed to upload image: " + e.getMessage());
        }
    }

    public void deleteImage(Long imageId) throws Exception {
        try {
            serverConnection.sendDeleteRequest(IMAGE_ENDPOINT + "/" + imageId);
        } catch (Exception e) {
            throw new Exception("Failed to delete image: " + e.getMessage());
        }
    }

    public ImageDTO getImage(Long imageId) throws Exception {
        try {
            String response = serverConnection.sendGetRequest(IMAGE_ENDPOINT + "/" + imageId);
            return gson.fromJson(response, ImageDTO.class);
        } catch (Exception e) {
            throw new Exception("Failed to get image: " + e.getMessage());
        }
    }

    private static class MultipartUploadDTO {
        private final String image;

        public MultipartUploadDTO(String base64Image) {
            this.image = base64Image;
        }
    }
}
