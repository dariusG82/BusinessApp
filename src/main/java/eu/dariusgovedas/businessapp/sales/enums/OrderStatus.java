package eu.dariusgovedas.businessapp.sales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
    OPEN - Order is processing
    INVOICED - Order is finalized, invoice is printed
    CONFIRMED - Orders payment is confirmed
    CANCELED - Order is canceled before Payment Received
    CREDITED - Order is canceled after Payment Received, Credit Invoice created
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    OPEN("OPEN"),
    INVOICED("INVOICED"),
    CONFIRMED("CONFIRMED"),
    CANCELED("CANCELED"),
    CREDITED("CREDITED");

    private final String status;
}
