package eu.dariusgovedas.businessapp.items.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ItemDTO {

    private Long itemNumber;
    private String name;
    private String category;
    private String description;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Long stockQuantity;

    private Long orderQuantity;
    private BigDecimal orderPrice;
}
