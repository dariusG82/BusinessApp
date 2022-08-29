package eu.dariusgovedas.businessapp.sales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderType {

    PURCHASE("PURCHASE"),
    SALE("SALE"),
    RETURN("RETURN");

    private final String type;
}
