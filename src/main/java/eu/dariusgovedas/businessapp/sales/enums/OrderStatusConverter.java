package eu.dariusgovedas.businessapp.sales.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus orderStatus) {
        if(orderStatus == null){
            return null;
        }

        return orderStatus.getStatus();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }

        return Stream.of(OrderStatus.values())
                .filter(orderStatus -> orderStatus.getStatus().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
