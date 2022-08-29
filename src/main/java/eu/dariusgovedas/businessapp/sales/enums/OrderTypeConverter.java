package eu.dariusgovedas.businessapp.sales.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderTypeConverter implements AttributeConverter<OrderType, String> {
    @Override
    public String convertToDatabaseColumn(OrderType orderType) {
        if(orderType == null){
            return null;
        }

        return orderType.getType();
    }

    @Override
    public OrderType convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }

        return Stream.of(OrderType.values())
                .filter(orderType -> orderType.getType().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
