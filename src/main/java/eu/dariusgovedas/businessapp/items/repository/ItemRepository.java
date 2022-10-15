package eu.dariusgovedas.businessapp.items.repository;

import eu.dariusgovedas.businessapp.items.entities.Item;
import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemByItemNumber(Long itemNumber);

    List<Item> findItemByCategory(ItemCategory category);

    Item findItemById(UUID itemID);
}
