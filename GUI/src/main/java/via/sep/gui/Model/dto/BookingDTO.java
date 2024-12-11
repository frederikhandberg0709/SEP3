package via.sep.gui.Model.dto;

import java.time.LocalDateTime;

public class BookingDTO {
    private Long bookingId;
    private Long propertyId;
    private Long agentId;
    private LocalDateTime bookingDate;
    private AgentDTO agent;

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }

    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public AgentDTO getAgent() { return agent; }
    public void setAgent(AgentDTO agent) { this.agent = agent; }
}
