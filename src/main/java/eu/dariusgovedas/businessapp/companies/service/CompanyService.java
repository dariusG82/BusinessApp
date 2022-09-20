package eu.dariusgovedas.businessapp.companies.service;

import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.entities.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.entities.ContactDetails;
import eu.dariusgovedas.businessapp.companies.entities.RegistrationAddress;
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

    private CompanyRepository companyRepository;

    @Transactional
    public void saveCompanyData(CompanyDTO companyDTO) {
        Company company = getClientFromClientDTO(companyDTO);

        companyRepository.save(company);
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

    private ContactDetails getContactDetails(CompanyDTO companyDTO) {
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setPhoneNumber(companyDTO.getPhoneNumber());
        contactDetails.setEmailAddress(companyDTO.getEmailAddress());
        return contactDetails;
    }

    public Page<CompanyDTO> getCompanies(Pageable pageable) {
        List<Company> companies = companyRepository.findAll();

        List<CompanyDTO> companyDTOS = getCompanyDTOsFromCompanyList(companies);

        return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
    }

    private List<CompanyDTO> getCompanyDTOsFromCompanyList(List<Company> companies){
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        for (Company company : companies) {
            companyDTOS.add(getClientDTOFromClient(company));
        }

        return companyDTOS;
    }

    private Company getClientFromClientDTO(CompanyDTO companyDTO) {
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

    private CompanyDTO getClientDTOFromClient(Company company) {
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

    public Page<CompanyDTO> searchForCompany(Pageable pageable, CompanyDTO companyDTO) {
        List<CompanyDTO> companyDTOS = new ArrayList<>();

        if (companyDTO.getCompanyID() != null) {
            Company company = companyRepository.findByCompanyID(companyDTO.getCompanyID());
            if(company == null){
                return new PageImpl<>(companyDTOS, pageable, 0);
            }
            if (checkCompanyName(company, companyDTO) && checkCountry(company, companyDTO) && checkCity(company, companyDTO)) {
                companyDTOS.add(getClientDTOFromClient(company));
                return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
            }
        }

        if (companyDTO.getCompanyName() != null) {
            List<Company> companies = companyRepository.findByCompanyNameContainingIgnoreCase(companyDTO.getCompanyName());
            for (Company company : companies) {
                if (checkCountry(company, companyDTO) && checkCity(company, companyDTO)) {
                    companyDTOS.add(getClientDTOFromClient(company));
                }
            }
            return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
        }

        if (companyDTO.getCountry() != null) {
            List<Company> companies = companyRepository.findByCountry(companyDTO.getCountry().toUpperCase());
            for (Company company : companies) {
                if (checkCity(company, companyDTO)) {
                    companyDTOS.add(getClientDTOFromClient(company));
                }
            }
            return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
        }

        if (companyDTO.getCity() != null) {
            List<Company> companies = companyRepository.findByCity(companyDTO.getCity().toUpperCase());
            for (Company company : companies) {
                companyDTOS.add(getClientDTOFromClient(company));
            }
            return new PageImpl<>(companyDTOS, pageable, companyDTOS.size());
        }

        return new PageImpl<>(companyDTOS, pageable, 0);
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

    public Company getCompanyById(Long id) {
        return companyRepository.findByCompanyID(id);
    }

    public CompanyDTO getCompanyDTOById(Long id) {
        Company company = getCompanyById(id);

        return getClientDTOFromClient(company);
    }

    @Transactional
    public void updateClient(Long id, CompanyDTO companyDTO) {
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

    public CompanyDTO getClientDTOByName(String clientName) {
        Company company = companyRepository.findByCompanyName(clientName);
        return getClientDTOFromClient(company);
    }
}
