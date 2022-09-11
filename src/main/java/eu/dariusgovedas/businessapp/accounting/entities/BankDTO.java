package eu.dariusgovedas.businessapp.accounting.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankDTO {

    private String bankName;
    private String bankSwift;
    private String bankAddress;
    private String bankAccountNumber;
}
