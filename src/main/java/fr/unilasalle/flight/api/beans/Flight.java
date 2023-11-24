package fr.unilasalle.flight.api.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.validation.constraints.NotNull;
import jakarta.enterprise.inject.Model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Model
@Table(name= "flights")
@Getter
@Setter
@NoArgsConstructor
public class Flight extends PanacheEntityBase {

    @Id
    @SequenceGenerator(
            name = "flights_sequence_in_java_code",
            sequenceName = "flights_sequence_in_database",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "flights_sequence_in_java_code"
    )
    private Long id;

    @NotNull(message = "The number should not be null")
    @Column(name = "number", nullable = false, unique = true)
    private String number;

    @NotNull(message = "The origin should be provided")
    @Column(name = "origin", nullable = false)
    private String origin;

    @NotNull(message = "The destination should be provided")
    @Column(name = "destination", nullable = false)
    private String destination;

    @NotNull(message = "The departure date should be provided")
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @NotNull(message = "The departure time should be provided")
    @Column(name = "departure_time", nullable = false)
    private LocalTime departureTime;

    @NotNull(message = "The arrival date should be provided")
    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

    @NotNull(message = "The arrival time should be provided")
    @Column(name = "arrival_time", nullable = false)
    private LocalTime arrivalTime;

    @NotNull(message = "The plane should be provided")
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "planeId", referencedColumnName = "id", nullable = false)
    private Plane plane;

    @JsonIgnore
    @OneToMany(targetEntity = Reservation.class, mappedBy = "flight")
    private List<Reservation> reservations = new ArrayList<>();
}