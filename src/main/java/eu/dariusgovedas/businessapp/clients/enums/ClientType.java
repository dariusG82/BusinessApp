package eu.dariusgovedas.businessapp.clients.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClientType {
    CUSTOMER("CUSTOMER"),
    SUPPLIER("SUPPLIER"),
    OWNER("OWNER");

    private final String value;
}
