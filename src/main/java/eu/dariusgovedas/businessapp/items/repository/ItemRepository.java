package eu.dariusgovedas.businessapp.items.repository;

import eu.dariusgovedas.businessapp.items.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemByItemNumber(Long itemNumber);
}
