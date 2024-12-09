package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "images", schema = "properties")
@Data
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    public void setProperty(Property property) {
        this.property = property;
        if (property != null && !property.getImages().contains(this)) {
            property.getImages().add(this);
        }
    }
}
