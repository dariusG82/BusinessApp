package eu.dariusgovedas.businessapp.common.exceptions;

import lombok.Getter;

@Getter
public class BankAccountNotFoundException extends RuntimeException{

    public BankAccountNotFoundException(){
        super("Bank Account cannot be found / Bank Account data is missing");
    }
}
