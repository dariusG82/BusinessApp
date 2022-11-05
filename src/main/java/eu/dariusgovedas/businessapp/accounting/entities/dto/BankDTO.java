package eu.dariusgovedas.businessapp.accounting.entities.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

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
}
