package via.sep.restful_server.api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.model.Apartment;
import via.sep.restful_server.model.House;
import via.sep.restful_server.model.Image;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.repository.ApartmentRepository;
import via.sep.restful_server.repository.HouseRepository;
import via.sep.restful_server.repository.ImageRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyRepository propertyRepository;
    private final HouseRepository houseRepository;
    private final ApartmentRepository apartmentRepository;
    private final ImageRepository imageRepository;

    public PropertyController(PropertyRepository propertyRepository,
                              HouseRepository houseRepository,
                              ApartmentRepository apartmentRepository,
                              ImageRepository imageRepository) {
        this.propertyRepository = propertyRepository;
        this.houseRepository = houseRepository;
        this.apartmentRepository = apartmentRepository;
        this.imageRepository = imageRepository;
    }

    @PostMapping
    public ResponseEntity<?> createProperty(@RequestBody PropertyDTO propertyDTO) {
        Property property = new Property();
        property.setPropertyType(propertyDTO.getPropertyType());
        property.setAddress(propertyDTO.getAddress());
        property.setFloorArea(propertyDTO.getFloorArea());
        property.setPrice(propertyDTO.getPrice());
        property.setNumBedrooms(propertyDTO.getNumBedrooms());
        property.setNumBathrooms(propertyDTO.getNumBathrooms());
        property.setYearBuilt(propertyDTO.getYearBuilt());
        property.setDescription(propertyDTO.getDescription());

        Property savedProperty = propertyRepository.save(property);

        if ("House".equals(propertyDTO.getPropertyType())) {
            House house = new House();
            house.setProperty(savedProperty);
            house.setLotSize(propertyDTO.getLotSize());
            house.setHasGarage(propertyDTO.getHasGarage());
            house.setNumFloors(propertyDTO.getNumFloors());
            houseRepository.save(house);
        } else if ("Apartment".equals(propertyDTO.getPropertyType())) {
            Apartment apartment = new Apartment();
            apartment.setProperty(savedProperty);
            apartment.setFloorNumber(propertyDTO.getFloorNumber());
            apartment.setBuildingName(propertyDTO.getBuildingName());
            apartment.setHasElevator(propertyDTO.getHasElevator());
            apartment.setHasBalcony(propertyDTO.getHasBalcony());
            apartmentRepository.save(apartment);
        }

        return ResponseEntity.ok(savedProperty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProperty(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(property -> {
                    PropertyDTO dto = new PropertyDTO();
                    dto.setPropertyType(property.getPropertyType());
                    dto.setAddress(property.getAddress());
                    dto.setFloorArea(property.getFloorArea());
                    dto.setPrice(property.getPrice());
                    dto.setNumBedrooms(property.getNumBedrooms());
                    dto.setNumBathrooms(property.getNumBathrooms());
                    dto.setYearBuilt(property.getYearBuilt());
                    dto.setDescription(property.getDescription());

                    if ("HOUSE".equals(property.getPropertyType())) {
                        houseRepository.findByProperty_PropertyId(id).ifPresent(house -> {
                            dto.setLotSize(house.getLotSize());
                            dto.setHasGarage(house.getHasGarage());
                            dto.setNumFloors(house.getNumFloors());
                        });
                    } else if ("APARTMENT".equals(property.getPropertyType())) {
                        apartmentRepository.findByProperty_PropertyId(id).ifPresent(apartment -> {
                            dto.setFloorNumber(apartment.getFloorNumber());
                            dto.setBuildingName(apartment.getBuildingName());
                            dto.setHasElevator(apartment.getHasElevator());
                            dto.setHasBalcony(apartment.getHasBalcony());
                        });
                    }

                    List<Long> imageIds = imageRepository.findByProperty_PropertyId(id)
                            .stream()
                            .map(Image::getId)
                            .collect(Collectors.toList());
                    dto.setImageIds(imageIds);

                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        return ResponseEntity.ok(propertyRepository.findAll());
    }

    // Image related endpoints

    @PostMapping("/{id}/images")
    public ResponseEntity<?> addImageToProperty(@PathVariable Long id,
                                                @RequestParam("image") MultipartFile file) {
        try {
            return propertyRepository.findById(id)
                    .map(property -> {
                        try {
                            Image image = new Image();
                            image.setProperty(property);
                            image.setImageData(file.getBytes());
                            Image savedImage = imageRepository.save(image);

                            return ResponseEntity.ok()
                                    .body(Map.of("message", "Image uploaded successfully",
                                            "imageId", savedImage.getId()));
                        } catch (IOException e) {
                            return ResponseEntity.internalServerError()
                                    .body("Failed to process image");
                        }
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to upload image");
        }
    }

    @GetMapping("/{propertyId}/images/{imageId}")
    public ResponseEntity<?> getPropertyImage(@PathVariable Long propertyId,
                                              @PathVariable Long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> {
                    if (!image.getProperty().getPropertyId().equals(propertyId)) {
                        return ResponseEntity.notFound().build();
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.IMAGE_JPEG)
                            .body(image.getImageData());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<Long>> getPropertyImageIds(@PathVariable Long id) {
        List<Long> imageIds = imageRepository.findByProperty_PropertyId(id)
                .stream()
                .map(Image::getId)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageIds);
    }

    @DeleteMapping("/{propertyId}/images/{imageId}")
    public ResponseEntity<?> deletePropertyImage(@PathVariable Long propertyId,
                                                 @PathVariable Long imageId) {
        return imageRepository.findById(imageId)
                .map(image -> {
                    if (!image.getProperty().getPropertyId().equals(propertyId)) {
                        return ResponseEntity.notFound().build();
                    }
                    imageRepository.delete(image);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(property -> {
                    propertyRepository.delete(property);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
