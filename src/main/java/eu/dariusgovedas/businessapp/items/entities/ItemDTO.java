package eu.dariusgovedas.businessapp.items.entities;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ItemDTO {

    private String name;
    private String description;
    private BigDecimal purchasePrice;
    private BigDecimal salePrice;
    private Long quantity;
}
