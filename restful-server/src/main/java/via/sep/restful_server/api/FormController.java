/*
package via.sep.restful_server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import via.sep.restful_server.dto.FormDTO;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.service.PropertyFormService;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/forms")
@Slf4j
public class FormController {


    private final PropertyFormService propertyFormService;
    private final PropertyController propertyController;

    public FormController(PropertyFormService propertyFormService, PropertyController propertyController) {
        this.propertyFormService = propertyFormService;
        this.propertyController = propertyController;
    }

    @PostMapping
    public ResponseEntity<?> submitForm(@RequestBody FormDTO formDTO) {
        try {
            // Step 1: Register the form (save it in the database and send notifications)
            propertyFormService.registerProperty(formDTO);

            // Step 2: Convert FormDTO to PropertyDTO for creating the property
            PropertyDTO propertyDTO = mapFormToProperty(formDTO);

            // Step 3: Call PropertyController to create the property
            return propertyController.createProperty(propertyDTO);
        } catch (Exception e) {
            log.error("Error while submitting form: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("An error occurred while submitting the form.");
        }
    }

    // Helper method to map FormDTO to PropertyDTO
    private PropertyDTO mapFormToProperty(FormDTO formDTO) {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setPropertyType(formDTO.getPropertyType());
        propertyDTO.setAddress(formDTO.getAddress());
        propertyDTO.setFloorArea(formDTO.getFloorArea());
        propertyDTO.setPrice(formDTO.getPrice());
        propertyDTO.setNumBedrooms(formDTO.getNumBedrooms());
        propertyDTO.setNumBathrooms(formDTO.getNumBathrooms());
        propertyDTO.setYearBuilt(formDTO.getYearBuilt());

        if ("House".equals(formDTO.getPropertyType())) {
            propertyDTO.setNumFloors(formDTO.getNumFloors());
            propertyDTO.setHasGarage(formDTO.getHasGarage());
        } else if ("Apartment".equals(formDTO.getPropertyType())) {
            propertyDTO.setFloorNumber(formDTO.getNumFloors());
            propertyDTO.setBuildingName(formDTO.getBuildingName());
            propertyDTO.setHasElevator(formDTO.getHasElevator());
            propertyDTO.setHasBalcony(formDTO.getHasBalcony());
        }
        return propertyDTO;
    }

}
*/
