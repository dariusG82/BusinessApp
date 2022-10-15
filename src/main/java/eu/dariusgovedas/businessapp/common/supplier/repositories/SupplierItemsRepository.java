package eu.dariusgovedas.businessapp.common.supplier.repositories;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SupplierItemsRepository extends JpaRepository<SupplierItem, Long> {
    @Query("FROM SupplierItem item WHERE item.warehouse.warehouseID=:id AND item.itemID=:uuid")
    SupplierItem findByWarehouseAndItemId(@Param("id") Long id, @Param("uuid") UUID itemID);
}
