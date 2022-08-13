package eu.dariusgovedas.businessapp.accounting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CashRecordType {
    SALES_RECORD("SALES RECORD"),
    RETURN_RECORD("RETURN RECORD"),
    PURCHASE_RECORD("PURCHASE RECORD");

    private final String record;
}
