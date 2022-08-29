package eu.dariusgovedas.businessapp.accounting.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class CashRecordTypeConverter implements AttributeConverter<CashRecordType, String> {

    @Override
    public String convertToDatabaseColumn(CashRecordType cashRecordType) {
        if(cashRecordType == null){
            return null;
        }

        return cashRecordType.getRecord();
    }

    @Override
    public CashRecordType convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }

        return Stream.of(CashRecordType.values())
                .filter(cashRecordType -> cashRecordType.getRecord().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
