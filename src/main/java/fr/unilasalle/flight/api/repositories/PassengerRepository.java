package fr.unilasalle.flight.api.repositories;

import fr.unilasalle.flight.api.beans.Passenger;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Model;

import java.util.List;

@Model
@ApplicationScoped
public class PassengerRepository implements PanacheRepositoryBase<Passenger, Long>{

    public List<Passenger> findBySurnameAndFirstName(String surname, String firstName) {
        return find("SELECT p FROM Passenger p WHERE p.surname = ?1 AND p.firstName = ?2", surname, firstName).stream().toList();
    }

    public List<Passenger> findByEmail(String email) {
        return find("email", email).stream().toList();
    }

    public void update(Passenger outdatedPassenger, Passenger updatedPassenger){

        // La méthode récurpère les données de l'objet passé en paramètre et les met à jour avec les données de l'objet passé en second paramètre

        outdatedPassenger.setSurname(updatedPassenger.getSurname());
        outdatedPassenger.setFirstName(updatedPassenger.getFirstName());
        outdatedPassenger.setEmail(updatedPassenger.getEmail());

        outdatedPassenger.persist();
    }
}