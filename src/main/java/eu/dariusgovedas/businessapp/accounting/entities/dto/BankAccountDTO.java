package eu.dariusgovedas.businessapp.accounting.entities.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Setter
@Getter
@Component
public class BankAccountDTO {

    private String accountNumber;
    private BigDecimal balance;
    private String bankName;
}
