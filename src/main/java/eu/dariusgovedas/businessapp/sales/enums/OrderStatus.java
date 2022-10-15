package eu.dariusgovedas.businessapp.sales.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
    OPEN - Order is processing
    INVOICED - Order is finalized, invoice is created
    CONFIRMED - Orders payment is confirmed
    FINISHED - Payment is received/made and Goods Movement Confirmed (reduce/increase Warehouse quantity)
    CANCELED - Order is canceled before Payment Received
    CREDITED - Order is canceled after Payment Received, Credit Invoice created
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {

    OPEN("OPEN"),
    INVOICED("INVOICED"),
    CONFIRMED("CONFIRMED"),
    FINISHED("FINISHED"),
    CANCELED("CANCELED"),
    CREDITED("CREDITED");

    private final String status;
}
