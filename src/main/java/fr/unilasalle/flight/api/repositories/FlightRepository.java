package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.beans.Plane;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
@ApplicationScoped
public class FlightRepository implements PanacheRepositoryBase<Flight, Long> {

    // Ici faire les query nécessaires pour récupérer les données de la table planes

    public Flight getById(Long id) {
        return findById(id);
    }

    public List<Flight> findByDestination(String destination) {
        return find("destination", destination).list();
    }
}
