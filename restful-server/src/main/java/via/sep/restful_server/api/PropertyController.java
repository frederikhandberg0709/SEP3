package via.sep.restful_server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.model.Apartment;
import via.sep.restful_server.model.House;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.notification.dto.PriceChangeNotificationDTO;
import via.sep.restful_server.notification.service.NotificationService;
import via.sep.restful_server.repository.ApartmentRepository;
import via.sep.restful_server.repository.HouseRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {
    private final PropertyRepository propertyRepository;
    private final HouseRepository houseRepository;
    private final ApartmentRepository apartmentRepository;
    private final NotificationService notificationService;

    @Autowired
    public PropertyController(PropertyRepository propertyRepository,
                              HouseRepository houseRepository,
                              ApartmentRepository apartmentRepository,
                              NotificationService notificationService) {
        this.propertyRepository = propertyRepository;
        this.houseRepository = houseRepository;
        this.apartmentRepository = apartmentRepository;
        this.notificationService = notificationService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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

        notificationService.notifyPropertyCreated(propertyDTO, savedProperty.getPropertyId().toString());

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

                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        return ResponseEntity.ok(propertyRepository.findAll());
    }

    // Change all fields
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @RequestBody PropertyDTO propertyDTO) {
        return propertyRepository.findById(id)
                .map(property -> {
                    BigDecimal oldPrice = property.getPrice(); // Saving old price for notification

                    property.setPropertyType(propertyDTO.getPropertyType());
                    property.setAddress(propertyDTO.getAddress());
                    property.setFloorArea(propertyDTO.getFloorArea());
                    property.setPrice(propertyDTO.getPrice());
                    property.setNumBedrooms(propertyDTO.getNumBedrooms());
                    property.setNumBathrooms(propertyDTO.getNumBathrooms());
                    property.setYearBuilt(propertyDTO.getYearBuilt());
                    property.setDescription(propertyDTO.getDescription());

                    Property updatedProperty = propertyRepository.save(property);

                    // Notify price change only if price has changed
                    if (!oldPrice.equals(propertyDTO.getPrice())) {
                        PriceChangeNotificationDTO notificationData = new PriceChangeNotificationDTO(
                                id.toString(),
                                property.getAddress(),
                                oldPrice,
                                propertyDTO.getPrice(),
                                LocalDateTime.now()
                        );
                        notificationService.notifyPriceChange(notificationData);
                    }

                    if ("House".equals(propertyDTO.getPropertyType())) {
                        houseRepository.findByProperty_PropertyId(id)
                                .ifPresent(house -> {
                                    house.setLotSize(propertyDTO.getLotSize());
                                    house.setHasGarage(propertyDTO.getHasGarage());
                                    house.setNumFloors(propertyDTO.getNumFloors());
                                    houseRepository.save(house);
                                });
                    } else if ("Apartment".equals(propertyDTO.getPropertyType())) {
                        apartmentRepository.findByProperty_PropertyId(id)
                                .ifPresent(apartment -> {
                                    apartment.setFloorNumber(propertyDTO.getFloorNumber());
                                    apartment.setBuildingName(propertyDTO.getBuildingName());
                                    apartment.setHasElevator(propertyDTO.getHasElevator());
                                    apartment.setHasBalcony(propertyDTO.getHasBalcony());
                                    apartmentRepository.save(apartment);
                                });
                    }

                    return ResponseEntity.ok(updatedProperty);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Only change price
    @PatchMapping("/{id}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePropertyPrice(
            @PathVariable Long id,
            @RequestBody BigDecimal newPrice) {
        return propertyRepository.findById(id)
                .map(property -> {
                    BigDecimal oldPrice = property.getPrice();
                    property.setPrice(newPrice);
                    Property updatedProperty = propertyRepository.save(property);

                    // Send price update notification
                    PriceChangeNotificationDTO notificationData = new PriceChangeNotificationDTO(
                            id.toString(),
                            property.getAddress(),
                            oldPrice,
                            newPrice,
                            LocalDateTime.now()
                    );
                    notificationService.notifyPriceChange(notificationData);

                    return ResponseEntity.ok(updatedProperty);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id) {
        return propertyRepository.findById(id)
                .map(property -> {

                    propertyRepository.delete(property);

                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
