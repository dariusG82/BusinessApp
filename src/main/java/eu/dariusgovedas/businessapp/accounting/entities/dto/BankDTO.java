package eu.dariusgovedas.businessapp.accounting.entities.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Setter
@Getter
@Component
public class BankDTO {

    @NotEmpty
    private String bankName;

    @NotEmpty
    private String bankSwift;

    @NotEmpty
    private String bankAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankDTO bankDTO = (BankDTO) o;
        return bankName.equals(bankDTO.bankName) && bankSwift.equals(bankDTO.bankSwift) && bankAddress.equals(bankDTO.bankAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankName, bankSwift, bankAddress);
    }
}
