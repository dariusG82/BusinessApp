package eu.dariusgovedas.businessapp.clients.repository;

import eu.dariusgovedas.businessapp.clients.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
