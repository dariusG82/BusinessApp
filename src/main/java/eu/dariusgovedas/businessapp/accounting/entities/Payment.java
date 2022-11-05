package eu.dariusgovedas.businessapp.accounting.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;
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
}
