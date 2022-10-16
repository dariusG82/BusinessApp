package eu.dariusgovedas.businessapp.items.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StockItem {

    @Id
    private Long stockID;

    private BigDecimal purchasePrice;

    private BigDecimal salePrice;

    private Long quantity;

    @ManyToOne
    private Item item;
}
