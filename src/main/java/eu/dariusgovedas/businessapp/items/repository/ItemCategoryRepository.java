package eu.dariusgovedas.businessapp.items.repository;

import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory, String> {
    ItemCategory findByCategoryNameContainingIgnoreCase(String category);
}
