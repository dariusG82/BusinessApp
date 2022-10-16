package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemPropertiesDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemPropertiesService {

    private ItemCategoryService categoryService;

    public List<String> getCategories() {
        List<ItemCategory> categories = categoryService.getAllCategories();
        List<String> results = new ArrayList<>();
        categories.forEach(itemCategory -> results.add(itemCategory.getCategoryName()));
        return results;
    }

    public ItemCategory getItemCategory(String categoryName){
        return categoryService.findCategoryByName(categoryName);
    }

    @Transactional
    public void saveNewCategory(ItemPropertiesDTO propertiesDTO) {
        ItemCategory itemCategory = new ItemCategory();
        String newCategoryName = propertiesDTO.getCategoryName();

        if(newCategoryName.equals("")){
            return;
        }

        if(getItemCategory(newCategoryName) == null){
            itemCategory.setId(categoryService.getNewCategoryID());
            itemCategory.setCategoryName(newCategoryName);
            categoryService.saveNewItemCategory(itemCategory);
        }
    }
}
