package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "agents", schema = "properties")
@Data
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "agent_id")
    private Long agentId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "contact_info", length = 255)
    private String contactInfo;

    @OneToMany(mappedBy = "agent")
    private Set<Booking> bookings;
}