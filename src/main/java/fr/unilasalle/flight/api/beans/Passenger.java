package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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

    @NotNull(message = "The firstname should not be null")
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @NotNull(message = "The surname should not be null")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull(message = "The email should not be null")
    @Column(name = "email_address", nullable = false, unique = true)
    @Email(message = "The email should be valid", regexp = "^[A-Za-z0-9+_.-]+@.+\\..+$")
    private String email;
}
