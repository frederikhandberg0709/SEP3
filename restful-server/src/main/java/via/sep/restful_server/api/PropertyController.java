/*package via.sep.restful_server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.model.Apartment;
import via.sep.restful_server.model.House;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.notification.dto.PriceChangeNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyNotificationDTO;
import via.sep.restful_server.notification.dto.PropertyUpdateNotificationDTO;
import via.sep.restful_server.notification.service.NotificationService;
import via.sep.restful_server.repository.ApartmentRepository;
import via.sep.restful_server.repository.HouseRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
            apartment.setNumFloors(propertyDTO.getNumFloors());
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
                    dto.setPropertyId(property.getPropertyId());
                    dto.setPropertyType(property.getPropertyType());
                    dto.setAddress(property.getAddress());
                    dto.setFloorArea(property.getFloorArea());
                    dto.setPrice(property.getPrice());
                    dto.setNumBedrooms(property.getNumBedrooms());
                    dto.setNumBathrooms(property.getNumBathrooms());
                    dto.setYearBuilt(property.getYearBuilt());
                    dto.setDescription(property.getDescription());

                    if ("House".equals(property.getPropertyType())) {
                        log.debug("Fetching house details for property ID: {}", id);

                        houseRepository.findByProperty_PropertyId(id).ifPresentOrElse(
                                house -> {
                                    log.debug("Found house: ID={}, NumFloors={}",
                                            house.getHouseId(),
                                            house.getNumFloors());

                                    dto.setLotSize(house.getLotSize());
                                    dto.setHasGarage(house.getHasGarage());
                                    dto.setNumFloors(house.getNumFloors());
                                },
                                () -> log.debug("No house found for property ID: {}", id)
                        );
                    } else if ("Apartment".equals(property.getPropertyType())) {
                        log.debug("Fetching apartment details for property ID: {}", id);

                        apartmentRepository.findByProperty_PropertyId(id).ifPresentOrElse(
                                apartment -> {
                                    log.debug("Found apartment: ID={}, NumFloors={}",
                                            apartment.getApartmentId(),
                                            apartment.getNumFloors());

                                    dto.setFloorNumber(apartment.getFloorNumber());
                                    dto.setBuildingName(apartment.getBuildingName());
                                    dto.setHasElevator(apartment.getHasElevator());
                                    dto.setHasBalcony(apartment.getHasBalcony());
                                    dto.setNumFloors(apartment.getNumFloors());
                                    dto.setLotSize(apartment.getLotSize());
                                    dto.setHasGarage(apartment.getHasGarage());
                                },
                                () -> log.debug("No apartment found for property ID: {}", id)
                        );
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

                    if ("HOUSE".equals(property.getPropertyType())) {
                        houseRepository.findByProperty_PropertyId(id).ifPresent(house -> {
                            house.setLotSize(propertyDTO.getLotSize());
                            house.setHasGarage(propertyDTO.getHasGarage());
                            house.setNumFloors(propertyDTO.getNumFloors());
                            houseRepository.save(house);
                        });
                    } else if ("APARTMENT".equals(property.getPropertyType())) {
                        apartmentRepository.findByProperty_PropertyId(id).ifPresent(apartment -> {
                            apartment.setFloorNumber(propertyDTO.getFloorNumber());
                            apartment.setBuildingName(propertyDTO.getBuildingName());
                            apartment.setHasElevator(propertyDTO.getHasElevator());
                            apartment.setHasBalcony(propertyDTO.getHasBalcony());
                            apartment.setNumFloors(propertyDTO.getNumFloors());
                            apartmentRepository.save(apartment);
                        });
                    }

                    Map<String, Object> updatedFields = createPropertyDetails(propertyDTO);

                    PropertyUpdateNotificationDTO notificationData = new PropertyUpdateNotificationDTO(
                            id.toString(),
                            property.getAddress(),
                            oldPrice,
                            propertyDTO.getPrice(),
                            property.getPropertyType(),
                            updatedFields,
                            LocalDateTime.now()
                    );

                    notificationService.notifyPropertyUpdated(notificationData);

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

    private Map<String, Object> createPropertyDetails(PropertyDTO propertyDTO) {
        Map<String, Object> details = new HashMap<>();
        details.put("numBedrooms", propertyDTO.getNumBedrooms());
        details.put("numBathrooms", propertyDTO.getNumBathrooms());
        details.put("floorArea", propertyDTO.getFloorArea());
        details.put("yearBuilt", propertyDTO.getYearBuilt());

        if ("House".equals(propertyDTO.getPropertyType())) {
            details.put("lotSize", propertyDTO.getLotSize());
            details.put("hasGarage", propertyDTO.getHasGarage());
            details.put("numFloors", propertyDTO.getNumFloors());
        } else if ("Apartment".equals(propertyDTO.getPropertyType())) {
            details.put("floorNumber", propertyDTO.getFloorNumber());
            details.put("buildingName", propertyDTO.getBuildingName());
            details.put("hasElevator", propertyDTO.getHasElevator());
            details.put("hasBalcony", propertyDTO.getHasBalcony());
            details.put("numFloors", propertyDTO.getNumFloors());
        }

        return details;
    }
}
*/