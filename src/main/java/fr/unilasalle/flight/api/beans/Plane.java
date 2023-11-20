package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "The operator should not be blank")
    @Column(name = "operator")
    private String operator;
    @Column(name = "model")
    private String model;
    @Column(name = "registration")
    private String registration;

    @NotNull(message = "The capacity should not be null")
    @Size(min = 6, max = 100, message = "The capacity should be between 1 and 100")
    @Column(name = "capacity")
    private String capacity;
}
