package via.sep.restful_server.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "login", schema = "accounts")
@Data
public class Login {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 20)
    private String role = "USER";

    @OneToOne(mappedBy = "login", cascade = CascadeType.ALL)
    private UserProfile userProfile;
}
