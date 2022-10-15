package eu.dariusgovedas.businessapp.common.supplier.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * A DTO for the {@link SupplierItem} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupplierItemDto implements Serializable {
    private UUID itemID;
    private Long itemNumber;
    private String itemName;
    private String itemDescription;
    private String category;
    private BigDecimal price;
    private Long quantity;
}