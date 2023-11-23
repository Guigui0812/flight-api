package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "reservations")
@Model
@Getter
@Setter
@NoArgsConstructor
public class Reservation extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "reservation_sequence_in_java_code",
            sequenceName = "reservation_sequence_in_database",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reservation_sequence_in_java_code"
    )
    private Long id;

    @NotNull(message = "The flight should not be null")
    @ManyToOne
    @JoinColumn(name = "flightId", referencedColumnName = "id", nullable = false)
    private Flight flight;

    @NotNull(message = "The passenger should not be null")
    @ManyToOne
    @JoinColumn(name = "passengerId", referencedColumnName = "id", nullable = false)
    private Passenger passenger;
}
