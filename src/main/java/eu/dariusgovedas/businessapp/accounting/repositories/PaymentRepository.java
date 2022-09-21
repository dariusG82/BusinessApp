package eu.dariusgovedas.businessapp.accounting.repositories;

import eu.dariusgovedas.businessapp.accounting.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
