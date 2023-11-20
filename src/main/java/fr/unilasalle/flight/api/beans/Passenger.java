package fr.unilasalle.flight.api.beans;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Passenger {

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
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "The surname should not be empty")
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotEmpty(message = "The email should not be empty")
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
