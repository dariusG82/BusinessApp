package eu.dariusgovedas.businessapp.common.exceptions;

import lombok.Getter;

@Getter
public class BankNotFoundException extends RuntimeException{

    public BankNotFoundException(){
        super("Bank cannot be found / Bank data is missing");
    }
}
