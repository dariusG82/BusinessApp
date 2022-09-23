package eu.dariusgovedas.businessapp.common.supplier.repositories;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierItemsRepository extends JpaRepository<SupplierItem, Long> {
}
