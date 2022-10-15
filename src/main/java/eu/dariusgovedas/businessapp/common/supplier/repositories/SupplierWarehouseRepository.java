package eu.dariusgovedas.businessapp.common.supplier.repositories;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierWarehouseRepository extends JpaRepository<SupplierWarehouse, Long> {

    @Query("FROM SupplierWarehouse w WHERE w.supplierName=:name")
    SupplierWarehouse findBySupplierName(@Param("name") String name);
}
