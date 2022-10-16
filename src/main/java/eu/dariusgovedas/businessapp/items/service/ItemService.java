package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.Item;
import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.entities.StockItem;
import eu.dariusgovedas.businessapp.items.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    public Page<ItemDTO> getWarehouseStock(Pageable pageable) {
        List<Item> items = itemRepository.findAll();

        List<StockItem> stockItems = getStockItems(items);

        List<ItemDTO> itemDTOList = getItemDTOList(stockItems);

        return new PageImpl<>(itemDTOList, pageable, itemDTOList.size());
    }

    private List<StockItem> getStockItems(List<Item> items) {
        List<StockItem> stockItemList = new ArrayList<>();
        for (Item item : items) {
            if (item.getStockItems().size() > 0) {
                List<StockItem> itemList = item.getStockItems();
                stockItemList.addAll(itemList);
            } else {
                StockItem stockItem = new StockItem();
                stockItem.setItem(item);
                stockItem.setPurchasePrice(BigDecimal.ZERO);
                stockItem.setSalePrice(BigDecimal.ZERO);
                stockItem.setQuantity(0L);
                stockItemList.add(stockItem);
            }
        }
        return stockItemList;
    }

    private List<ItemDTO> getItemDTOList(List<StockItem> stockItemList) {
        List<ItemDTO> itemDTOList = new ArrayList<>();
        for (StockItem stockItem : stockItemList) {
            ItemDTO itemDTO = new ItemDTO();

            itemDTO.setItemNumber(stockItem.getItem().getItemNumber());
            itemDTO.setItemID(stockItem.getItem().getId());
            itemDTO.setName(stockItem.getItem().getName());
            itemDTO.setCategory(stockItem.getItem().getCategory().getCategoryName());
            itemDTO.setDescription(stockItem.getItem().getDescription());
            itemDTO.setPurchasePrice(stockItem.getPurchasePrice());
            itemDTO.setSalePrice(stockItem.getSalePrice());
            itemDTO.setStockQuantity(stockItem.getQuantity());

            itemDTOList.add(itemDTO);
        }
        return itemDTOList;
    }

    public UUID getItemID(Long itemNumber) {
        return itemRepository.findItemByItemNumber(itemNumber).getId();
    }
}
