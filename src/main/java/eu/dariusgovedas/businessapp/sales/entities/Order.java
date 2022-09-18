package eu.dariusgovedas.businessapp.sales.entities;

import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private LocalDate orderDate;

    private String clientName;

    private String supplierName;

    private BigDecimal orderAmount;

    private BigDecimal vatAmount;

    private BigDecimal amountWithVAT;

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

    public BigDecimal getOrderAmount() {
        BigDecimal total = BigDecimal.ZERO;

        if(!(orderLines == null)){
            for(OrderLine line : orderLines){
                total = total.add(BigDecimal.valueOf(line.getQuantity()).multiply(line.getPurchasePrice()));
            }
        }

        return total;
    }

    public BigDecimal getVatAmount() {
        BigDecimal amount = getOrderAmount();
        BigDecimal taxAmount = BigDecimal.valueOf(21);
        BigDecimal percentage = BigDecimal.valueOf(100);

        return amount.multiply(taxAmount).divide(percentage, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmountWithVAT() {
        return getOrderAmount().add(getVatAmount());
    }
}
