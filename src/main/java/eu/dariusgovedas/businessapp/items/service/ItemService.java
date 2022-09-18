package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.Item;
import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.ItemDTO;
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

@Service
@AllArgsConstructor
public class ItemService {

    private ItemRepository itemRepository;
    private ItemPropertiesService itemPropertiesService;

    @Transactional
    public void createItem(ItemDTO itemDTO) {
        Item item = new Item();
        ItemCategory category = itemPropertiesService.getItemCategory(itemDTO.getCategory());

        item.setId(generateID());
        item.setItemNumber(generateItemNumber(category.getId(), item.getId()));
        item.setCategory(category);
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());

        itemRepository.save(item);
    }

    private Long generateItemNumber(Long catID, Long itemID) {
        return catID * 100000 + itemID;
    }

    private Long generateID() {
        long itemsCount = itemRepository.count();
        return itemsCount + 1;
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

    public ItemDTO getItemDTOByItemNumber(Long number){
        Item item = itemRepository.findItemByItemNumber(number);

        return item == null ? new ItemDTO() : getItemDTOFromItem(item);
    }

    private ItemDTO getItemDTOFromItem(Item item) {
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setItemNumber(item.getItemNumber());
        itemDTO.setName(item.getName());
        itemDTO.setCategory(item.getCategory().getCategoryName());
        itemDTO.setDescription(item.getDescription());
        BigDecimal salePrice = getSalePrice(item);
        itemDTO.setSalePrice(salePrice);
        Long quantity = getQuantity(item, salePrice);
        itemDTO.setStockQuantity(quantity);

        return itemDTO;
    }

    private BigDecimal getSalePrice(Item item) {
        List<StockItem> stockItems = item.getStockItems();
        for(StockItem stockItem : stockItems){
            if(stockItem.getQuantity() > 0){
                return stockItem.getSalePrice();
            }
        }
        return BigDecimal.ZERO;
    }

    private Long getQuantity(Item item, BigDecimal salePrice) {
        if(salePrice.equals(BigDecimal.ZERO)){
            return 0L;
        }
        return item.getStockItems().stream()
                .filter(stockItem -> stockItem.getSalePrice().equals(salePrice))
                .findFirst()
                .orElseThrow().getQuantity();

    }

}
