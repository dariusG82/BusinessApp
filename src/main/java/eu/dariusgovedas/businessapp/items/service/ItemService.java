package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.Item;
import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ItemService {

    private ItemRepository itemRepository;

    private ItemPropertiesService itemPropertiesService;

    @Transactional
    public void createItem(ItemDTO itemDTO) {
        Item item = new Item();
        ItemCategory category = itemPropertiesService.getItemCategory(itemDTO.getCategory());

        if(itemDTO.getItemID() != null){
            item.setId(itemDTO.getItemID());
        } else {
            item.setId(UUID.randomUUID());
        }
        if(itemDTO.getItemNumber() == null){
            item.setItemNumber(generateItemNumber(category));
        } else {
            item.setItemNumber(itemDTO.getItemNumber());
        }
        item.setCategory(category);
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());

        itemRepository.save(item);
    }

    public Long generateItemNumber(ItemCategory category) {
        long itemNr = itemRepository.findItemByCategory(category).size() + 1L;
        return category.getId() * 100000 + itemNr;
    }

    public UUID getItemID(Long itemNumber) {
        return itemRepository.findItemByItemNumber(itemNumber).getId();
    }

    public List<Item> getWarehouseItems() {
        return itemRepository.findAll();
    }

    public Item getItemByItemName(String itemName){
        return itemRepository.findByName(itemName);
    }

    public void updateItem(Item item){
        itemRepository.save(item);
    }
}
