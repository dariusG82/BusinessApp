package eu.dariusgovedas.businessapp.common.supplier.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
public class SupplierItem {

    @Id
    private UUID itemID;
    private Long itemNumber;
    private String itemName;
    private String itemDescription;
    private String category;
    private BigDecimal price;
    private Long quantity;

    @ManyToOne
    private SupplierWarehouse warehouse;
}
