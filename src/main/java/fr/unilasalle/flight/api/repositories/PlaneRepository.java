package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Plane;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
@ApplicationScoped
public class PlaneRepository implements PanacheRepositoryBase<Plane, Long> {

    public List<Plane> findByOperator(String operator) {
        return find("operator", operator).stream().toList();
    }

    public Plane getById(Long id) {
        return findById(id);
    }
}
