package eu.dariusgovedas.businessapp.sales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    OPEN("OPEN"),
    CONFIRMED("CONFIRMED"),
    FINISHED("FINISHED"),
    CANCELED("CANCELED");

    private final String status;
}
