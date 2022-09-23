package eu.dariusgovedas.businessapp.items.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {

    @Id
    private UUID id;

    private Long itemNumber;

    private String name;

    private String description;

    @ManyToOne
    private ItemCategory category;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "item",
            orphanRemoval = true
    )
    private List<StockItem> stockItems;

    public void addStockItem(StockItem stockItem){
        stockItems.add(stockItem);
        stockItem.setItem(this);
    }

    public void removeStockItem(StockItem stockItem){
        stockItems.remove(stockItem);
        stockItem.setItem(null);
    }
}
