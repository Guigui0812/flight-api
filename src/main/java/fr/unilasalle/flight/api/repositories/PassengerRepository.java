package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Passenger;
import fr.unilasalle.flight.api.beans.Plane;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Model;

@Model
@ApplicationScoped
public class PassengerRepository implements PanacheRepositoryBase<Passenger, Long>{

    public Passenger findByEmailAddress(String emailAddress) {
        return find("emailAddress", emailAddress).firstResultOptional().orElse(null);
    }

}