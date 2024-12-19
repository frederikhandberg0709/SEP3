package via.sep.restful_server.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import via.sep.restful_server.api.NotificationController;
import via.sep.restful_server.model.Notification;
import via.sep.restful_server.notification.service.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    private Notification testNotification;

    @BeforeEach
    void setUp() {
        testNotification = new Notification();
        testNotification.setNotificationId(1L);
        testNotification.setAccountId(1L);
        testNotification.setType("TEST_TYPE");
        testNotification.setMessage("Test notification message");
        testNotification.setTimestamp(LocalDateTime.now());
        testNotification.setRead(false);
        testNotification.setReferenceId("REF-1");
        testNotification.setDetails("{\"test\": \"details\"}");
    }

    @Test
    void getNotificationHistory_Success() throws Exception {
        when(notificationService.getNotificationHistory(1L))
                .thenReturn(Arrays.asList(testNotification));

        mockMvc.perform(get("/api/notifications")
                        .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].notificationId").value(testNotification.getNotificationId()))
                .andExpect(jsonPath("$[0].accountId").value(testNotification.getAccountId()))
                .andExpect(jsonPath("$[0].type").value(testNotification.getType()))
                .andExpect(jsonPath("$[0].message").value(testNotification.getMessage()))
                .andExpect(jsonPath("$[0].read").value(testNotification.isRead()))
                .andExpect(jsonPath("$[0].referenceId").value(testNotification.getReferenceId()))
                .andExpect(jsonPath("$[0].details").value(testNotification.getDetails()));
    }

    @Test
    void getNotificationHistory_EmptyList() throws Exception {
        when(notificationService.getNotificationHistory(1L))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/notifications")
                        .param("accountId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getNotificationHistory_MissingAccountId() throws Exception {
        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getNotificationHistory_InvalidAccountId() throws Exception {
        mockMvc.perform(get("/api/notifications")
                        .param("accountId", "invalid"))
                .andExpect(status().isBadRequest());
    }
}
