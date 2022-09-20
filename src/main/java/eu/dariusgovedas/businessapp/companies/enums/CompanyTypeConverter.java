package eu.dariusgovedas.businessapp.companies.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class CompanyTypeConverter implements AttributeConverter<CompanyType, String> {

    @Override
    public String convertToDatabaseColumn(CompanyType companyType) {
        if(companyType == null){
            return null;
        }

        return companyType.getValue();
    }

    @Override
    public CompanyType convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }

        return Stream.of(CompanyType.values())
                .filter(clientType -> clientType.getValue().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
