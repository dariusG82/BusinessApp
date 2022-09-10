package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.repository.ItemCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemCategoryService {

    ItemCategoryRepository categoryRepository;
    public List<String> getCategories() {
        List<ItemCategory> categories = categoryRepository.findAll();
        List<String> results = new ArrayList<>();
        categories.forEach(itemCategory -> results.add(itemCategory.getCategoryName()));
        return results;
    }
}
