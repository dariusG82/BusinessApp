package eu.dariusgovedas.businessapp.items.repository;

import eu.dariusgovedas.businessapp.items.entities.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {

}
