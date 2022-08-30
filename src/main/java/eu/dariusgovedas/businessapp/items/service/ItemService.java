package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.Item;
import eu.dariusgovedas.businessapp.items.entities.ItemDTO;
import eu.dariusgovedas.businessapp.items.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ItemService {

    private ItemRepository itemRepository;

    @Transactional
    public void createItem(ItemDTO itemDTO) {
        Item item = new Item();

        item.setId(generateID());
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());

        itemRepository.save(item);
    }

    private Long generateID() {
        long itemsCount = itemRepository.count();
        return itemsCount + 1;
    }
}
