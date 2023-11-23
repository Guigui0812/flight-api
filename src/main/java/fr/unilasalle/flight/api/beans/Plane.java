package fr.unilasalle.flight.api.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "planes")
@Getter
@Setter
@NoArgsConstructor
public class Plane extends PanacheEntityBase {

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
    @Column(name = "operator", nullable = false)
    private String operator;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "registration", nullable = false, unique = true)
    private String registration;

    @NotNull(message = "The capacity should not be null")
    @Min(value = 10, message = "The capacity should be greater than 0")
    @Max(value = 20, message = "The capacity should be less than 20")
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
}
