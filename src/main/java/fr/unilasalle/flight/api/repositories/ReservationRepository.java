package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Flight;
import fr.unilasalle.flight.api.beans.Reservation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
@ApplicationScoped
public class ReservationRepository implements PanacheRepositoryBase<Reservation, Long> {

    public List<Reservation> findByFlightNumber(String number) {
        return find("flight.number", number).list();
    }

    public List<Reservation> findByFlightId(Long id) {
        return find("flight.id", id).list();
    }

    public Long countByFlightNumber(String number) {
        return count("flight.number", number);
    }
}
