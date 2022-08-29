package eu.dariusgovedas.businessapp.sales.entities;

import eu.dariusgovedas.businessapp.items.entities.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLine {

    @Id
    private Long id;

    private Long lineNumber;

    private String itemName;

    private Long quantity;

    private BigDecimal purchasePrice;

    @ManyToOne
    private Order order;
}
