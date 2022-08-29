package eu.dariusgovedas.businessapp.sales.entities;

import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private OrderType orderType;

    private LocalDate localDate;

    private String clientName;

    private String supplierName;

    private BigDecimal orderAmount;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderLine> orderLines;

    public void addOrderLine(OrderLine orderLine){
        orderLines.add(orderLine);
        orderLine.setOrder(this);
    }

    public void removeOrderLine(OrderLine orderLine){
        orderLines.remove(orderLine);
        orderLine.setOrder(null);
    }
}
