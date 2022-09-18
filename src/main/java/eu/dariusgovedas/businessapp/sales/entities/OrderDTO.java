package eu.dariusgovedas.businessapp.sales.entities;

import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class OrderDTO {

    private Long orderNumber;
    private OrderType orderType;
    private LocalDate orderDate;
    private String client;
    private String supplier;
    private BigDecimal amount;
    private BigDecimal vatAmount;
    private BigDecimal amountWithVAT;
    private OrderStatus status;
}
