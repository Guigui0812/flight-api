package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

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

}
