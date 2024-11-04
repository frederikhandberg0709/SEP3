package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "images", schema = "properties")
@Data
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "image_data", nullable = false)
    private byte[] imageData;
}
