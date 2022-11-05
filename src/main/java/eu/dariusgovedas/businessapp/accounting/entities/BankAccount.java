package eu.dariusgovedas.businessapp.accounting.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Component
public class BankAccount {

    @Id
    private Long id;
    private String number;
    private BigDecimal balance;

    @ManyToOne
    private Bank bank;
}
