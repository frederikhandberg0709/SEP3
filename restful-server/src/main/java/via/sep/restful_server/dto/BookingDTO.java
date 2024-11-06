package via.sep.restful_server.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDTO {
    private Long propertyId;
    private Long agentId;
    private LocalDate bookingDate;
}
