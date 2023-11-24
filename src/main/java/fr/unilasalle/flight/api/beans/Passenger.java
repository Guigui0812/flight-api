package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Model
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "passengers")
public class Passenger extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "passenger_sequence_in_java_code",
            sequenceName = "passenger_sequence_in_database",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "passenger_sequence_in_java_code"
    )
    private Long id;

    @NotEmpty(message = "The firstname should not be empty")
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @NotEmpty(message = "The surname should not be empty")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotEmpty(message = "The email should not be empty")
    @Column(name = "email_address", nullable = false)
    private String email;

    // Faire les réversations (un passager peut avoir plusieurs réservations)

}
