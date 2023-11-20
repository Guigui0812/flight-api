package fr.unilasalle.flight.api.beans;

import jakarta.persistence.*;

public class Reservation {

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

    @Column(name = "flight_id")
    private Flight flight;

    @Column(name = "passenger_id")
    private Passenger passenger;

}
