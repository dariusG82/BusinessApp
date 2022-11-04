package eu.dariusgovedas.businessapp.companies.service;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierWarehouse;
import eu.dariusgovedas.businessapp.common.supplier.services.SupplierWarehouseService;
import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.entities.ContactDetails;
import eu.dariusgovedas.businessapp.companies.entities.RegistrationAddress;
import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import eu.dariusgovedas.businessapp.companies.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final SupplierWarehouseService warehouseService;

    @Transactional
    public void saveCompanyData(CompanyDTO companyDTO) {
        Company company = getCompanyFromCompanyDTO(companyDTO);

        companyRepository.save(company);

        if(company.getCompanyType().equals(CompanyType.SUPPLIER)){
            saveNewSupplier(company);
        }
    }

    public Page<CompanyDTO> getCompanies(Pageable pageable) {
        List<Company> companies = companyRepository.findAll();

        List<CompanyDTO> companyDTOS = getCompanyDTOsFromCompanyList(companies);

        return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
    }

    public Page<CompanyDTO> searchForCompany(Pageable pageable, CompanyDTO companyDTO) {
        List<CompanyDTO> companyDTOS = new ArrayList<>();

//        if(companyDTO.getCompanyType() == CompanyType.SUPPLIER
//                && companyDTO.getCompanyID() == null
//                && companyDTO.getCompanyType() == null){
//            List<Company> companies = companyRepository.findByCompanyType(companyDTO.getCompanyType());
//            for(Company company : companies){
//                companyDTOS.add(getCompanyDTOFromCompany(company));
//            }
//            return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
//        }

        if (companyDTO.getCompanyID() != null) {
            return findCompanyDTOsByID(companyDTO, pageable);
        }

        if (companyDTO.getCompanyName() != null) {
            return findCompanyDTOsByName(companyDTO, pageable);
        }

        if (companyDTO.getCountry() != null) {
            return findCompanyDTOsByCountry(companyDTO, pageable);
        }

        if (companyDTO.getCity() != null) {
            return findCompanyDTOsByCity(companyDTO, pageable);
        }

        return new PageImpl<>(companyDTOS, pageable, 0);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findByCompanyID(id);
    }

    public CompanyDTO getCompanyDTOById(Long id) {
        Company company = getCompanyById(id);

        return getCompanyDTOFromCompany(company);
    }

    @Transactional
    public void updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findByCompanyID(id);
        company.setCompanyName(companyDTO.getCompanyName());
        company.getRegistrationAddress().setCountry(companyDTO.getCountry());
        company.getRegistrationAddress().setCity(companyDTO.getCity());
        company.getRegistrationAddress().setStreet(companyDTO.getStreet());
        company.getRegistrationAddress().setHouseNumber(companyDTO.getHouseNumber());
        company.getRegistrationAddress().setFlatNumber(companyDTO.getFlatNumber());
        company.getContactDetails().setPhoneNumber(companyDTO.getPhoneNumber());
        company.getContactDetails().setEmailAddress(companyDTO.getEmailAddress());
    }

    public Company getBusinessOwner() {
        List<Company> companies = companyRepository.findByCompanyType(CompanyType.OWNER);
        return companies.get(0);
    }

    public CompanyDTO getCompanyDTOByName(String clientName) {
        Company company = companyRepository.findByCompanyName(clientName);
        if(company == null){
            return null;
        }
        return getCompanyDTOFromCompany(company);
    }

    public Page<CompanyDTO> getSuppliers(Pageable pageable) {
        List<Company> companies = companyRepository.findByCompanyType(CompanyType.SUPPLIER);

        List<CompanyDTO> companyDTOS = getCompanyDTOsFromCompanyList(companies);

        return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
    }

    @Transactional
    public void saveNewSupplier(Company company) {
        SupplierWarehouse supplierWarehouse = new SupplierWarehouse();
        supplierWarehouse.setWarehouseID(warehouseService.getNewWarehouseNumber());
        supplierWarehouse.setSupplierName(company.getCompanyName());
        supplierWarehouse.setSupplierItemList(new ArrayList<>());

        warehouseService.saveNewWarehouse(supplierWarehouse);
    }

    private List<CompanyDTO> getCompanyDTOsFromCompanyList(List<Company> companies){
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        for (Company company : companies) {
            companyDTOS.add(getCompanyDTOFromCompany(company));
        }

        return companyDTOS;
    }

    private Company getCompanyFromCompanyDTO(CompanyDTO companyDTO) {
        Company company = new Company();
        company.setCompanyID(companyDTO.getCompanyID());
        company.setCompanyName(companyDTO.getCompanyName());
        company.setCompanyType(companyDTO.getCompanyType());

        RegistrationAddress registrationAddress = getRegistrationAddress(companyDTO);

        ContactDetails contactDetails = getContactDetails(companyDTO);

        company.setRegistrationAddress(registrationAddress);
        company.setContactDetails(contactDetails);

        return company;
    }

    private ContactDetails getContactDetails(CompanyDTO companyDTO) {
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setPhoneNumber(companyDTO.getPhoneNumber());
        contactDetails.setEmailAddress(companyDTO.getEmailAddress());
        return contactDetails;
    }

    private RegistrationAddress getRegistrationAddress(CompanyDTO companyDTO) {
        RegistrationAddress registrationAddress = new RegistrationAddress();
        registrationAddress.setCountry(companyDTO.getCountry());
        registrationAddress.setCity(companyDTO.getCity());
        registrationAddress.setStreet(companyDTO.getStreet());
        registrationAddress.setHouseNumber(companyDTO.getHouseNumber());
        registrationAddress.setFlatNumber(companyDTO.getFlatNumber());
        return registrationAddress;
    }

    private CompanyDTO getCompanyDTOFromCompany(Company company) {
        CompanyDTO companyDTO = new CompanyDTO();

        companyDTO.setCompanyID(company.getCompanyID());
        companyDTO.setCompanyName(company.getCompanyName());
        companyDTO.setCompanyType(company.getCompanyType());
        companyDTO.setCountry(company.getRegistrationAddress().getCountry());
        companyDTO.setCity(company.getRegistrationAddress().getCity());
        companyDTO.setStreet(company.getRegistrationAddress().getStreet());
        companyDTO.setHouseNumber(company.getRegistrationAddress().getHouseNumber());
        companyDTO.setFlatNumber(company.getRegistrationAddress().getFlatNumber());
        companyDTO.setPhoneNumber(company.getContactDetails().getPhoneNumber());
        companyDTO.setEmailAddress(company.getContactDetails().getEmailAddress());

        return companyDTO;
    }

    private Page<CompanyDTO> findCompanyDTOsByID(CompanyDTO companyDTO, Pageable pageable) {
        List<CompanyDTO> companyDTOS = new ArrayList<>();
        Company company = companyRepository.findByCompanyID(companyDTO.getCompanyID());
        if(company == null){
            return new PageImpl<>(companyDTOS, pageable, 0);
        }
        if (checkCompanyName(company, companyDTO)
                && checkCompanyType(company, companyDTO)
                && checkCountry(company, companyDTO)
                && checkCity(company, companyDTO)) {
            companyDTOS.add(getCompanyDTOFromCompany(company));
            return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
        }
        return new PageImpl<>(companyDTOS, pageable, 0);
    }

    private Page<CompanyDTO> findCompanyDTOsByName(CompanyDTO companyDTO, Pageable pageable) {
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        List<Company> companies = companyRepository.findByCompanyNameContainingIgnoreCase(companyDTO.getCompanyName());
        for (Company company : companies) {
            if (checkCompanyType(company, companyDTO) && checkCountry(company, companyDTO) && checkCity(company, companyDTO)) {
                companyDTOS.add(getCompanyDTOFromCompany(company));
            }
        }
        return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
    }

    private Page<CompanyDTO> findCompanyDTOsByCountry(CompanyDTO companyDTO, Pageable pageable) {
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        List<Company> companies = companyRepository.findByCountry(companyDTO.getCountry().toUpperCase());
        for (Company company : companies) {
            if (checkCity(company, companyDTO)) {
                companyDTOS.add(getCompanyDTOFromCompany(company));
            }
        }
        return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
    }

    private Page<CompanyDTO> findCompanyDTOsByCity(CompanyDTO companyDTO, Pageable pageable) {
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        List<Company> companies = companyRepository.findByCity(companyDTO.getCity().toUpperCase());
        for (Company company : companies) {
            companyDTOS.add(getCompanyDTOFromCompany(company));
        }
        return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
    }

    private boolean checkCompanyType(Company company, CompanyDTO companyDTO){
        if(companyDTO.getCompanyType() == null){
            return true;
        } else {
            return company.getCompanyType().equals(companyDTO.getCompanyType());
        }
    }

    private boolean checkCompanyName(Company company, CompanyDTO companyDTO) {
        if (companyDTO.getCompanyName() == null) {
            return true;
        } else {
            return company.getCompanyName().toUpperCase().contains(companyDTO.getCompanyName().toUpperCase());
        }
    }

    private boolean checkCountry(Company company, CompanyDTO companyDTO) {
        if (companyDTO.getCountry() == null) {
            return true;
        } else {
            return company.getRegistrationAddress().getCountry().toUpperCase().contains(companyDTO.getCountry().toUpperCase());
        }
    }

    private boolean checkCity(Company company, CompanyDTO companyDTO) {
        if (companyDTO.getCity() == null) {
            return true;
        } else {
            return company.getRegistrationAddress().getCity().toUpperCase().contains(companyDTO.getCity().toUpperCase());
        }
    }
}
