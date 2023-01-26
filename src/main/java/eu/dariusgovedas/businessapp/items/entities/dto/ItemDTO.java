package eu.dariusgovedas.businessapp.items.entities.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Component
public class ItemDTO {

    private UUID itemID;
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
