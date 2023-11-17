package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "planes")
@Getter
@Setter
@NoArgsConstructor
public class Plane extends PanacheEntity {

    @Id
    @SequenceGenerator(
            name = "planes_sequence_in_java_code",
            sequenceName = "planes_sequence_in_database",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "planes_sequence_in_java_code"
    )
    private Long id;
    @Column(name = "operator")
    private String operator;
    @Column(name = "model")
    private String model;
    @Column(name = "registration")
    private String registration;
    @Column(name = "capacity")
    private String capacity;
}
