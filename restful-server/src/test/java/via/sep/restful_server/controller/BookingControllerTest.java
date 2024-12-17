package via.sep.restful_server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import via.sep.restful_server.dto.BookingDTO;
import via.sep.restful_server.model.Agent;
import via.sep.restful_server.model.Booking;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.repository.AgentRepository;
import via.sep.restful_server.repository.BookingRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingRepository bookingRepository;

    @MockBean
    private PropertyRepository propertyRepository;

    @MockBean
    private AgentRepository agentRepository;

    @MockBean
    private JwtDecoder jwtDecoder;

    private Booking testBooking;
    private Property testProperty;
    private Agent testAgent;
    private BookingDTO testBookingDTO;
    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() {
        testProperty = new Property();
        testProperty.setPropertyId(1L);
        testProperty.setPropertyType("Apartment");
        testProperty.setAddress("123 Test St");

        testAgent = new Agent();
        testAgent.setAgentId(1L);
        testAgent.setName("Test Agent");

        testBooking = new Booking();
        testBooking.setBookingId(1L);
        testBooking.setProperty(testProperty);
        testBooking.setAgent(testAgent);
        testBooking.setBookingDate(LocalDate.now());

        testBookingDTO = new BookingDTO();
        testBookingDTO.setPropertyId(1L);
        testBookingDTO.setAgentId(1L);
        testBookingDTO.setBookingDate(LocalDate.now());

        adminToken = "mock.admin.token";
        userToken = "mock.user.token";

        Jwt adminJwt = Jwt.withTokenValue(adminToken)
                .header("alg", "HS256")
                .subject("admin")
                .claim("role", "ADMIN")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        Jwt userJwt = Jwt.withTokenValue(userToken)
                .header("alg", "HS256")
                .subject("user")
                .claim("role", "USER")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtDecoder.decode(adminToken)).thenReturn(adminJwt);
        when(jwtDecoder.decode(userToken)).thenReturn(userJwt);
    }

    @Test
    void createBooking_Success() throws Exception {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(agentRepository.findById(1L)).thenReturn(Optional.of(testAgent));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        mockMvc.perform(post("/api/bookings")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookingDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").value(testBooking.getBookingId()));
    }

    @Test
    void getBookings_ShouldReturnAllBookings() throws Exception {
        when(bookingRepository.findAll()).thenReturn(Arrays.asList(testBooking));

        mockMvc.perform(get("/api/bookings")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(testBooking.getBookingId()))
                .andExpect(jsonPath("$[0].property.propertyId").value(testProperty.getPropertyId()))
                .andExpect(jsonPath("$[0].agent.agentId").value(testAgent.getAgentId()));
    }

    @Test
    void getBookings_WithDate_ShouldReturnBookingsForDate() throws Exception {
        LocalDate testDate = LocalDate.now();
        when(bookingRepository.findByBookingDate(testDate)).thenReturn(Arrays.asList(testBooking));

        mockMvc.perform(get("/api/bookings")
                        .header("Authorization", "Bearer " + userToken)
                        .param("date", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookingId").value(testBooking.getBookingId()));
    }

    @Test
    void cancelBooking_AsAdmin_Success() throws Exception {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        mockMvc.perform(delete("/api/bookings/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void cancelBooking_AsUser_ShouldBeForbidden() throws Exception {
        mockMvc.perform(delete("/api/bookings/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void cancelBooking_BookingNotFound() throws Exception {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/bookings/999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
