package eu.dariusgovedas.businessapp.accounting.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Setter
@Getter
@Component
public class Payment {

    @Id
    private UUID ID;
    private Long orderNumber;
    private String customerName;
    private String supplierName;
    private LocalDate dateOfOrder;
    private LocalDate dateOfPayment;
    private BigDecimal paymentAmount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return orderNumber.equals(payment.orderNumber) && customerName.equals(payment.customerName) && supplierName.equals(payment.supplierName) && dateOfOrder.equals(payment.dateOfOrder) && dateOfPayment.equals(payment.dateOfPayment) && paymentAmount.equals(payment.paymentAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNumber, customerName, supplierName, dateOfOrder, dateOfPayment, paymentAmount);
    }
}
