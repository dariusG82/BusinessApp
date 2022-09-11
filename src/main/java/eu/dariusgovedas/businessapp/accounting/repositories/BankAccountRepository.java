package eu.dariusgovedas.businessapp.accounting.repositories;

import eu.dariusgovedas.businessapp.accounting.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
