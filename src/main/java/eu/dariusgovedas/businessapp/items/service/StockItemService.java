package eu.dariusgovedas.businessapp.items.service;

import eu.dariusgovedas.businessapp.items.entities.StockItem;
import eu.dariusgovedas.businessapp.items.repository.StockItemRepository;
import eu.dariusgovedas.businessapp.sales.entities.OrderLine;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class StockItemService {

    private StockItemRepository stockItemRepository;

    @Transactional
    public void saveStockItem(StockItem stockItem) {
        stockItemRepository.save(stockItem);
    }

    public StockItem createStockItem(OrderLine orderLine) {
        StockItem stockItem = new StockItem();

        stockItem.setStockID(stockItemRepository.count() + 1);
        stockItem.setPurchasePrice(orderLine.getPurchasePrice());
        stockItem.setSalePrice(calculateSalePrice(orderLine.getPurchasePrice()));
        stockItem.setQuantity(orderLine.getQuantity());

        return stockItem;
    }

    private BigDecimal calculateSalePrice(BigDecimal purchasePrice) {
        return purchasePrice.multiply(BigDecimal.valueOf(1.25));
    }
}
