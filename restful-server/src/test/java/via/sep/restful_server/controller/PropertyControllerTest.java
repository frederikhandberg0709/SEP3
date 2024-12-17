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
import via.sep.restful_server.dto.PropertyDTO;
import via.sep.restful_server.model.Apartment;
import via.sep.restful_server.model.House;
import via.sep.restful_server.model.Property;
import via.sep.restful_server.notification.service.NotificationService;
import via.sep.restful_server.repository.ApartmentRepository;
import via.sep.restful_server.repository.HouseRepository;
import via.sep.restful_server.repository.PropertyRepository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PropertyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PropertyRepository propertyRepository;

    @MockBean
    private HouseRepository houseRepository;

    @MockBean
    private ApartmentRepository apartmentRepository;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private JwtDecoder jwtDecoder;

    private Property testProperty;
    private House testHouse;
    private Apartment testApartment;
    private PropertyDTO testHouseDTO;
    private PropertyDTO testApartmentDTO;
    private String adminToken;
    private String userToken;

    @BeforeEach
    void setUp() {
        testProperty = new Property();
        testProperty.setPropertyId(1L);
        testProperty.setPropertyType("House");
        testProperty.setAddress("123 Test St");
        testProperty.setFloorArea(BigDecimal.valueOf(200.0));
        testProperty.setPrice(BigDecimal.valueOf(300000));
        testProperty.setNumBedrooms(3);
        testProperty.setNumBathrooms(2);
        testProperty.setYearBuilt(2020);
        testProperty.setDescription("Test Property");

        testHouse = new House();
        testHouse.setHouseId(1L);
        testHouse.setProperty(testProperty);
        testHouse.setLotSize(BigDecimal.valueOf(500.0));
        testHouse.setHasGarage(true);
        testHouse.setNumFloors(2);

        testHouseDTO = new PropertyDTO();
        testHouseDTO.setPropertyType("House");
        testHouseDTO.setAddress("123 Test St");
        testHouseDTO.setFloorArea(BigDecimal.valueOf(200.0));
        testHouseDTO.setPrice(BigDecimal.valueOf(300000));
        testHouseDTO.setNumBedrooms(3);
        testHouseDTO.setNumBathrooms(2);
        testHouseDTO.setYearBuilt(2020);
        testHouseDTO.setDescription("Test Property");
        testHouseDTO.setLotSize(BigDecimal.valueOf(500.0));
        testHouseDTO.setHasGarage(true);
        testHouseDTO.setNumFloors(2);

        testApartmentDTO = new PropertyDTO();
        testApartmentDTO.setPropertyType("Apartment");
        testApartmentDTO.setAddress("456 Test Ave");
        testApartmentDTO.setFloorArea(BigDecimal.valueOf(100.0));
        testApartmentDTO.setPrice(BigDecimal.valueOf(200000));
        testApartmentDTO.setNumBedrooms(2);
        testApartmentDTO.setNumBathrooms(1);
        testApartmentDTO.setYearBuilt(2020);
        testApartmentDTO.setDescription("Test Apartment");
        testApartmentDTO.setFloorNumber(3);
        testApartmentDTO.setBuildingName("Test Building");
        testApartmentDTO.setHasElevator(true);
        testApartmentDTO.setHasBalcony(true);
        testApartmentDTO.setNumFloors(5);

        adminToken = "admin.test.token";
        userToken = "user.test.token";

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
    void createProperty_AsAdmin_House_Success() throws Exception {
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(houseRepository.save(any(House.class))).thenReturn(testHouse);

        mockMvc.perform(post("/api/properties")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHouseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.propertyId").exists())
                .andExpect(jsonPath("$.propertyType").value("House"));

        verify(notificationService).notifyPropertyCreated(any(), any());
    }

    @Test
    void createProperty_AsUser_Forbidden() throws Exception {
        mockMvc.perform(post("/api/properties")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHouseDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void getProperty_Success() throws Exception {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(houseRepository.findByProperty_PropertyId(1L)).thenReturn(Optional.of(testHouse));

        mockMvc.perform(get("/api/properties/1")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.propertyId").value(1))
                .andExpect(jsonPath("$.propertyType").value("House"))
                .andExpect(jsonPath("$.lotSize").exists())
                .andExpect(jsonPath("$.hasGarage").exists());
    }

    @Test
    void getAllProperties_Success() throws Exception {
        when(propertyRepository.findAll()).thenReturn(Arrays.asList(testProperty));
        when(houseRepository.findByProperty_PropertyId(1L)).thenReturn(Optional.of(testHouse));

        mockMvc.perform(get("/api/properties")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].propertyId").value(1))
                .andExpect(jsonPath("$[0].propertyType").value("House"));
    }

    @Test
    void updateProperty_AsAdmin_Success() throws Exception {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);
        when(houseRepository.findByProperty_PropertyId(1L)).thenReturn(Optional.of(testHouse));

        mockMvc.perform(put("/api/properties/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testHouseDTO)))
                .andExpect(status().isOk());

        verify(notificationService).notifyPropertyUpdated(any());
    }

    @Test
    void updatePropertyPrice_AsAdmin_Success() throws Exception {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));
        when(propertyRepository.save(any(Property.class))).thenReturn(testProperty);

        mockMvc.perform(patch("/api/properties/1/price")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("350000"))
                .andExpect(status().isOk());

        verify(notificationService).notifyPriceChange(any());
    }

    @Test
    void deleteProperty_AsAdmin_Success() throws Exception {
        when(propertyRepository.findById(1L)).thenReturn(Optional.of(testProperty));

        mockMvc.perform(delete("/api/properties/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        verify(propertyRepository).delete(any(Property.class));
    }
}
