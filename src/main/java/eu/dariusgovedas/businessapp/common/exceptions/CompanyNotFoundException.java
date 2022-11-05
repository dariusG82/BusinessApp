package eu.dariusgovedas.businessapp.common.exceptions;

import lombok.Getter;

@Getter
public class CompanyNotFoundException extends RuntimeException {

    public CompanyNotFoundException() {
        super("Customer does not exist in database");
    }
}
