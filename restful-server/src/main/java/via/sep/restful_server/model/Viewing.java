package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Viewing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dateTime;
    private String contactName;
    private String contactEmail;
    private String contactPhone;

    @ManyToOne
    private Property property;
}
