package eu.dariusgovedas.businessapp.sales.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Setter
@Getter
@Component
public class OrderLineDTO {

    private Long itemNumber;
    private String itemName;
    private Long stockQuantity;
    private Long orderQuantity;
    private BigDecimal purchasePrice;
    private BigDecimal linePrice;
    private Long orderID;
}
