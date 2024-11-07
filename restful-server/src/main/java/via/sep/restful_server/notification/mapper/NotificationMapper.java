package via.sep.restful_server.notification.mapper;

import org.springframework.stereotype.Component;
import via.sep.restful_server.dto.AgentDTO;
import via.sep.restful_server.dto.BookingDTO;
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.notification.dto.AgentNotificationData;
import via.sep.restful_server.notification.dto.BookingNotificationData;
import via.sep.restful_server.notification.dto.NotificationDTO;
import via.sep.restful_server.notification.dto.PropertyNotificationData;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationMapper {
    public NotificationDTO toPropertyNotification(PropertyDTO propertyDTO, String action, String propertyId) {
        PropertyNotificationData data = new PropertyNotificationData(
                propertyId,
                propertyDTO.getAddress(),
                propertyDTO.getPrice(),
                propertyDTO.getPropertyType(),
                createPropertyDetails(propertyDTO)
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

    public NotificationDTO toBookingNotification(BookingDTO bookingDTO, String action, String bookingId) {
        BookingNotificationData data = new BookingNotificationData(
                bookingId,
                bookingDTO.getPropertyId().toString(),
                bookingDTO.getAgentId().toString(),
                bookingDTO.getBookingDate().atStartOfDay(),
                "CREATED" // or other status
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
        AgentNotificationData data = new AgentNotificationData(
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
}
