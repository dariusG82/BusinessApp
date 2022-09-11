package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.ItemPropertiesDTO;
import eu.dariusgovedas.businessapp.items.repository.ItemCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemPropertiesService {

    ItemCategoryRepository categoryRepository;

    public List<String> getCategories() {
        List<ItemCategory> categories = categoryRepository.findAll();
        List<String> results = new ArrayList<>();
        categories.forEach(itemCategory -> results.add(itemCategory.getCategoryName()));
        return results;
    }

    public ItemCategory getItemCategory(String categoryName){
        return categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
    }

    @Transactional
    public void saveNewCategory(ItemPropertiesDTO propertiesDTO) {
        ItemCategory itemCategory = new ItemCategory();
        String newCategoryName = propertiesDTO.getCategoryName();

        if(getItemCategory(newCategoryName) == null){
            itemCategory.setId(categoryRepository.count() + 1);
            itemCategory.setCategoryName(newCategoryName);
            categoryRepository.save(itemCategory);
        }
    }
}
