package via.sep.restful_server.notification.mapper;

import org.springframework.stereotype.Component;
import via.sep.restful_server.dto.AgentDTO;
import via.sep.restful_server.dto.BookingDTO;
import via.sep.restful_server.dto.PropertyDTO;
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
        BookingNotificationDTO data = new BookingNotificationDTO(
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
}
