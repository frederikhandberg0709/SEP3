package via.sep.gui.Model.dto;

import via.sep.gui.Model.domain.Agent;
import via.sep.gui.Model.domain.Booking;
import via.sep.gui.Model.domain.Property;

public class BookingResponseDTO {
    private Long bookingId;
    private Property property;
    private Agent agent;
    private String bookingDate;

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

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Booking toDomainBooking() {
        return new Booking(
                bookingId,
                property,
                agent,
                bookingDate
        );
    }
}
