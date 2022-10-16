package eu.dariusgovedas.businessapp.warehouse.services;

import eu.dariusgovedas.businessapp.items.entities.Item;
import eu.dariusgovedas.businessapp.items.entities.StockItem;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.service.ItemService;
import eu.dariusgovedas.businessapp.items.service.StockItemService;
import eu.dariusgovedas.businessapp.sales.entities.OrderLine;
import eu.dariusgovedas.businessapp.sales.services.OrderLineService;
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
public class WarehouseService {

    private ItemService itemService;
    private StockItemService stockItemService;
    private OrderLineService orderLineService;
    public Page<ItemDTO> getWarehouseStock(Pageable pageable) {
        List<Item> items = itemService.getWarehouseItems();

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

    @Transactional
    public void updateWarehouseStock(Long orderNr) {
        List<OrderLine> orderLines = orderLineService.getOrderLinesByOrder(orderNr);

        for(OrderLine orderLine : orderLines){
            Item item = itemService.getItemByItemName(orderLine.getItemName());
            List<StockItem> stockItems = item.getStockItems();
            if(stockItems.size() == 0){
                StockItem stockItem = stockItemService.createStockItem(orderLine);
                stockItem.setItem(item);
                stockItemService.saveStockItem(stockItem);
                continue;
            }
            for (StockItem stockItem : stockItems){
                if(stockItem.getPurchasePrice().equals(orderLine.getPurchasePrice())){
                    long quantity = stockItem.getQuantity() + orderLine.getQuantity();
                    stockItem.setQuantity(quantity);
                    stockItemService.saveStockItem(stockItem);
                }
            }
        }
    }
}
