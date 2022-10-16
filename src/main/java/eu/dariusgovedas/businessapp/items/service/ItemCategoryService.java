package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.repository.ItemCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemCategoryService {

    private ItemCategoryRepository categoryRepository;

    public List<ItemCategory> getAllCategories(){
        return categoryRepository.findAll();
    }

    public ItemCategory findCategoryByName(String categoryName){
        return categoryRepository.findByCategoryNameContainingIgnoreCase(categoryName);
    }

    public Long getNewCategoryID(){
        return categoryRepository.count() + 1;
    }

    public void saveNewItemCategory(ItemCategory category){
        categoryRepository.save(category);
    }
}
