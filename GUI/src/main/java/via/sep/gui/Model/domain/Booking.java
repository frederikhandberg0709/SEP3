package via.sep.gui.Model.domain;

import java.time.LocalDate;

public class Booking {
    private Long bookingId;
    private Property property;
    private Agent agent;
    private String bookingDate;

    public Booking(Long bookingId, Property property, Agent agent, String bookingDate) {
        this.bookingId = bookingId;
        this.property = property;
        this.agent = agent;
        this.bookingDate = bookingDate;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Property getProperty() {
        return property;
    }

    public Agent getAgent() {
        return agent;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getPropertyName() {
        return property != null ? property.getAddress() : "N/A";
    }

    public String getAgentName() {
        return agent != null ? agent.getName() : "N/A";
    }
}
