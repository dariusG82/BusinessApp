package eu.dariusgovedas.businessapp.common.supplier.repositories;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierWarehouseRepository extends JpaRepository<SupplierWarehouse, Long> {
    SupplierWarehouse findBySupplierName(String name);
}
