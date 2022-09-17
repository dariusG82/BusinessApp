package eu.dariusgovedas.businessapp.clients.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class ClientTypeConverter implements AttributeConverter<ClientType, String> {

    @Override
    public String convertToDatabaseColumn(ClientType clientType) {
        if(clientType == null){
            return null;
        }

        return clientType.getValue();
    }

    @Override
    public ClientType convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }

        return Stream.of(ClientType.values())
                .filter(clientType -> clientType.getValue().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
