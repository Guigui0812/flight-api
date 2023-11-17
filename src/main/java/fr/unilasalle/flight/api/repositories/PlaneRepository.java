package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Plane;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
public class PlaneRepository implements PanacheRepositoryBase<Plane, Long> {

    // Ici faire les query nécessaires pour récupérer les données de la table planes

    public Plane getById(Long id) {
        return findById(id);
    }

    public List<Plane> findByOperator(String operator) {
        return find("operator", operator).list();
    }
}
