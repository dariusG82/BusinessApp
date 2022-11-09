package eu.dariusgovedas.businessapp.accounting.entities.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@Getter
@Component
public class BankAccountDTO {

    private Long accountId;
    private String accountNumber;
    private BigDecimal balance;
    private String bankName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccountDTO that = (BankAccountDTO) o;
        return Objects.equals(accountId, that.accountId) && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(balance, that.balance) && Objects.equals(bankName, that.bankName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, accountNumber, balance, bankName);
    }
}
