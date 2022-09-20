package eu.dariusgovedas.businessapp.companies.service;

import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyPropertiesService {
    public List<CompanyType> getCompanyTypes() {
        List<CompanyType> companyTypes = new ArrayList<>();

        companyTypes.add(CompanyType.CUSTOMER);
        companyTypes.add(CompanyType.SUPPLIER);

        return companyTypes;
    }
}
