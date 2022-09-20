package eu.dariusgovedas.businessapp.companies.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CompanyType {
    CUSTOMER("CUSTOMER"),
    SUPPLIER("SUPPLIER"),
    OWNER("OWNER");

    private final String value;
}
