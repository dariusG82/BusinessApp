package eu.dariusgovedas.businessapp.clients.repository;

import eu.dariusgovedas.businessapp.clients.entities.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("FROM Client c WHERE c.businessID=:id OR UPPER(c.businessName) LIKE %:name% OR UPPER(c.registrationAddress.country) LIKE %:country% OR UPPER(c.registrationAddress.city) LIKE %:city%")
    List<Client> findClients(
            @Param("id") Long businessID,
            @Param("name") String businessName,
            @Param("country") String country,
            @Param("city") String city
    );

    Client findByBusinessID(Long businessID);

    List<Client> findByBusinessNameContainingIgnoreCase(String businessName);

    @Query("FROM Client c WHERE UPPER(c.registrationAddress.country) LIKE %:country%")
    List<Client> findByCountry(@Param("country") String country);

    @Query("FROM Client c WHERE UPPER(c.registrationAddress.city) LIKE %:city%")
    List<Client> findByCity(@Param("city") String city);
}
