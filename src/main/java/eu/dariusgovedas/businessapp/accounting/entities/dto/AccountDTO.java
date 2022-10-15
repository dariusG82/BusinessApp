package eu.dariusgovedas.businessapp.accounting.entities.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class AccountDTO {

    private String accountNumber;
    private BigDecimal balance;
    private String bankName;
}
