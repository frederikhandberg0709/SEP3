/*package via.sep.restful_server.notification.mapper;

import org.springframework.stereotype.Component;
import via.sep.restful_server.dto.*;
import via.sep.restful_server.model.Forms;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.notification.dto.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationMapper {
    public NotificationDTO toPropertyNotification(PropertyDTO propertyDTO, String action, String propertyId) {
        PropertyNotificationDTO data = new PropertyNotificationDTO(
                propertyId,
                propertyDTO.getAddress(),
                propertyDTO.getPrice(),
                propertyDTO.getPropertyType(),
                createPropertyDetails(propertyDTO),
                LocalDateTime.now()
        );

        return new NotificationDTO(
                "PROPERTY",
                action,
                propertyId,
                LocalDateTime.now(),
                data
        );
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
        }

        return details;
    }

    public NotificationDTO toBookmarkNotification(BookmarkDTO bookmarkDTO, PropertyDTO propertyDTO, String action, String bookmarkId) {
        BookmarkNotificationDTO data = new BookmarkNotificationDTO(
                bookmarkId,
                bookmarkDTO.getPropertyId().toString(),
                bookmarkDTO.getAccountId().toString(),
                propertyDTO.getAddress(),
                propertyDTO.getPrice(),
                propertyDTO.getPropertyType(),
                createPropertyDetails(propertyDTO),
                LocalDateTime.now()
        );

        return new NotificationDTO(
                "BOOKMARK",
                action,
                bookmarkId,
                LocalDateTime.now(),
                data
        );
    }

    public NotificationDTO toBookingNotification(BookingDTO bookingDTO, String action, String bookingId) {
        BookingNotificationDTO data = new BookingNotificationDTO(
                bookingId,
                bookingDTO.getPropertyId().toString(),
                bookingDTO.getAgentId().toString(),
                bookingDTO.getBookingDate().atStartOfDay(),
                "CREATED", // or other status
                LocalDateTime.now()
        );

        return new NotificationDTO(
                "BOOKING",
                action,
                bookingId,
                LocalDateTime.now(),
                data
        );
    }

    public NotificationDTO toAgentNotification(AgentDTO agentDTO, String action, String agentId) {
        AgentNotificationDTO data = new AgentNotificationDTO(
                agentId,
                agentDTO.getName(),
                agentDTO.getContactInfo()
        );

        return new NotificationDTO(
                "AGENT",
                action,
                agentId,
                LocalDateTime.now(),
                data
        );
    }

    public NotificationDTO toPriceChangeNotification(PropertyDTO propertyDTO, BigDecimal oldPrice, String propertyId) {
        PriceChangeNotificationDTO data = new PriceChangeNotificationDTO(
                propertyId,
                propertyDTO.getAddress(),
                oldPrice,
                propertyDTO.getPrice(),
                LocalDateTime.now()
        );

        return new NotificationDTO(
                "PROPERTY",
                "PRICE_CHANGE",
                propertyId,
                LocalDateTime.now(),
                data
        );
    }
    public NotificationDTO toFormNotification(Forms form, String action, String formId) {
        Map<String, Object> formDetails = createFormDetails(form);

        FormNotificationDTO data = new FormNotificationDTO(
                formId,
                form.getPropertyType(),
                form.getAddress(),
                form.getPrice(),
                formDetails,
                LocalDateTime.now()
        );

        return new NotificationDTO(
                "FORM",
                action,
                formId,
                LocalDateTime.now(),
                data
        );
    }

    private Map<String, Object> createFormDetails(Forms form) {
        Map<String, Object> details = new HashMap<>();
        details.put("floorArea", form.getFloorArea());
        details.put("numBedrooms", form.getNumBedrooms());
        details.put("numBathrooms", form.getNumBathrooms());
        details.put("yearBuilt", form.getYearBuilt());

        if ("House".equals(form.getPropertyType())) {
            details.put("numFloors", form.getNumFloors());
            details.put("hasGarage", form.getHasGarage());
        } else if ("Apartment".equals(form.getPropertyType())) {
            details.put("buildingName", form.getBuildingName());
            details.put("hasElevator", form.getHasElevator());
            details.put("hasBalcony", form.getHasBalcony());
        }

        details.put("description", form.getDescription());
        return details;
    }
    public Forms convertToFormDTO(FormDTO formDTO) {
        Forms form = new Forms();
        form.setPropertyType(formDTO.getPropertyType());
        form.setAddress(formDTO.getAddress());
        form.setPrice(formDTO.getPrice());
        form.setFloorArea(formDTO.getFloorArea());
        form.setNumBedrooms(formDTO.getNumBedrooms());
        form.setNumBathrooms(formDTO.getNumBathrooms());
        form.setYearBuilt(formDTO.getYearBuilt());
        form.setDescription(formDTO.getDescription());

        if ("House".equals(formDTO.getPropertyType())) {
            form.setNumFloors(formDTO.getNumFloors());
            form.setHasGarage(formDTO.getHasGarage());
        } else if ("Apartment".equals(formDTO.getPropertyType())) {
            form.setBuildingName(formDTO.getBuildingName());
            form.setHasElevator(formDTO.getHasElevator());
            form.setHasBalcony(formDTO.getHasBalcony());
        }

        return form;
    }
}
*/