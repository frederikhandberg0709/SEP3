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
import via.sep.restful_server.dto.AgentDTO;
import via.sep.restful_server.model.Agent;
import via.sep.restful_server.repository.AgentRepository;

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
public class AgentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgentRepository agentRepository;

    @MockBean
    private JwtDecoder jwtDecoder;

    private Agent testAgent;
    private AgentDTO testAgentDTO;
    private String adminToken;

    @BeforeEach
    void setup() {
        testAgent = new Agent();
        testAgent.setAgentId(1L);
        testAgent.setName("John Doe");
        testAgent.setContactInfo("john@example.com");

        testAgentDTO = new AgentDTO();
        testAgentDTO.setName("John Doe");
        testAgentDTO.setContactInfo("john@example.com");

        adminToken = "mock.admin.token";
        Jwt jwt = Jwt.withTokenValue(adminToken)
                .header("alg", "HS256")
                .subject("admin")
                .claim("role", "ADMIN")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(jwtDecoder.decode(any(String.class))).thenReturn(jwt);
    }

    @Test
    void createAgent_ShouldCreateAndReturnAgent() throws Exception {
        when(agentRepository.save(any(Agent.class))).thenReturn(testAgent);

        mockMvc.perform(post("/api/agents")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAgentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.agentId").value(testAgent.getAgentId()))
                .andExpect(jsonPath("$.name").value(testAgent.getName()))
                .andExpect(jsonPath("$.contactInfo").value(testAgent.getContactInfo()));
    }

    @Test
    void getAllAgents_ShouldReturnAllAgents() throws Exception {
        when(agentRepository.findAll()).thenReturn(Arrays.asList(testAgent));

        mockMvc.perform(get("/api/agents")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].agentId").value(testAgent.getAgentId()))
                .andExpect(jsonPath("$[0].name").value(testAgent.getName()))
                .andExpect(jsonPath("$[0].contactInfo").value(testAgent.getContactInfo()));
    }

    @Test
    void getAgent_WhenExists_ShouldReturnAgent() throws Exception {
        when(agentRepository.findById(1L)).thenReturn(Optional.of(testAgent));

        mockMvc.perform(get("/api/agents/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testAgent.getName()));
    }

    @Test
    void getAgent_WhenNotExists_ShouldReturn404() throws Exception {
        when(agentRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/agents/999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAvailableAgents_ShouldReturnAvailableAgents() throws Exception {
        LocalDate testDate = LocalDate.now();
        when(agentRepository.findAvailableAgentsByDate(testDate))
                .thenReturn(Arrays.asList(testAgent));

        mockMvc.perform(get("/api/agents/available")
                        .header("Authorization", "Bearer " + adminToken)
                        .param("date", testDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(testAgent.getName()));
    }

    @Test
    void updateAgent_WhenExists_ShouldUpdateAndReturnAgent() throws Exception {
        when(agentRepository.findById(1L)).thenReturn(Optional.of(testAgent));
        when(agentRepository.save(any(Agent.class))).thenReturn(testAgent);

        mockMvc.perform(put("/api/agents/1")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAgentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testAgent.getName()));
    }

    @Test
    void updateAgent_WhenNotExists_ShouldReturn404() throws Exception {
        when(agentRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/agents/999")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAgentDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAgent_WhenExists_ShouldReturn204() throws Exception {
        when(agentRepository.findById(1L)).thenReturn(Optional.of(testAgent));

        mockMvc.perform(delete("/api/agents/1")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAgent_WhenNotExists_ShouldReturn404() throws Exception {
        when(agentRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/agents/999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());
    }
}
