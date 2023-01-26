package eu.dariusgovedas.businessapp.services;

import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.entities.ContactDetails;
import eu.dariusgovedas.businessapp.companies.entities.RegistrationAddress;
import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import eu.dariusgovedas.businessapp.companies.repository.CompanyRepository;
import eu.dariusgovedas.businessapp.companies.service.CompanyPropertiesService;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyPropertiesService companyPropertiesService;

    @Autowired
    private CompanyDTO companyDTO;
    @Autowired
    private Company company;

    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.delete.companies}")
    private String sqlDeleteCompaniesData;

    public static Pageable pageable;

    @BeforeAll
    public static void beforeAllTests() {
        pageable = Pageable.ofSize(5);
    }

    @BeforeEach
    public void setup() {
        companyDTO.setCompanyID(123456789L);
        companyDTO.setCompanyName("MB Kankorezis");
        companyDTO.setCompanyType(CompanyType.SUPPLIER);
        companyDTO.setCountry("Lithuania");
        companyDTO.setCity("Kaunas");
        companyDTO.setStreet("V. Kudirkos g.");
        companyDTO.setHouseNumber("35a");
        companyDTO.setFlatNumber("25");
        companyDTO.setPhoneNumber("+370 626 70080");
        companyDTO.setEmailAddress("kankorezis@mail.com");

        company.setCompanyID(123456789L);
        company.setCompanyName("MB Kankorezis");
        company.setCompanyType(CompanyType.SUPPLIER);
        company.setRegistrationAddress(new RegistrationAddress());
        company.getRegistrationAddress().setCountry("Lithuania");
        company.getRegistrationAddress().setCity("Kaunas");
        company.getRegistrationAddress().setStreet("V. Kudirkos g.");
        company.getRegistrationAddress().setHouseNumber("35a");
        company.getRegistrationAddress().setFlatNumber("25");
        company.setContactDetails(new ContactDetails());
        company.getContactDetails().setPhoneNumber("+370 626 70080");
        company.getContactDetails().setEmailAddress("kankorezis@mail.com");

        companyRepository.save(company);

        companyDTO.setCompanyID(987654321L);
        companyDTO.setCompanyName("UAB 'Kita planeta'");
        companyDTO.setCompanyType(CompanyType.CUSTOMER);
        companyDTO.setCountry("Lithuania");
        companyDTO.setCity("Vilnius");
        companyDTO.setStreet("A. Brazausko al.");
        companyDTO.setHouseNumber("22");
        companyDTO.setPhoneNumber("+370 677 89955");
        companyDTO.setEmailAddress("planeta@mail.com");

        company.setCompanyID(987654321L);
        company.setCompanyName("UAB 'Kita planeta'");
        company.setCompanyType(CompanyType.CUSTOMER);
        company.setRegistrationAddress(new RegistrationAddress());
        company.getRegistrationAddress().setCountry("Lithuania");
        company.getRegistrationAddress().setCity("Vilnius");
        company.getRegistrationAddress().setStreet("A. Brazausko al.");
        company.getRegistrationAddress().setHouseNumber("22");
        company.setContactDetails(new ContactDetails());
        company.getContactDetails().setPhoneNumber("+370 677 89955");
        company.getContactDetails().setEmailAddress("planeta@mail.com");

        companyRepository.save(company);
    }

    @AfterEach
    public void cleanUpDatabase(){
        jdbc.execute(sqlDeleteCompaniesData);
    }

    @Test
    public void saveCompanyTest() {
        CompanyDTO newCompanyDTO = new CompanyDTO();
        newCompanyDTO.setCompanyID(123123123L);
        newCompanyDTO.setCompanyName("AB 'Saules takas'");
        newCompanyDTO.setCompanyType(CompanyType.CUSTOMER);
        newCompanyDTO.setCountry("Lithuania");
        newCompanyDTO.setCity("Klaipeda");
        newCompanyDTO.setStreet("Gedimino pr.");
        newCompanyDTO.setHouseNumber("125");
        newCompanyDTO.setPhoneNumber("+370 626 62626");
        newCompanyDTO.setEmailAddress("takas@mail.com");

        companyService.saveCompanyData(newCompanyDTO);
        List<Company> companies = companyRepository.findAll();

        assertEquals(3, companies.size());
    }

    @Test
    public void saveCompanyTest_CompanyTypeSUPPLIER(){
        CompanyDTO newCompanyDTO = new CompanyDTO();
        newCompanyDTO.setCompanyID(123123123L);
        newCompanyDTO.setCompanyName("AB 'Saules takas'");
        newCompanyDTO.setCompanyType(CompanyType.SUPPLIER);
        newCompanyDTO.setCountry("Lithuania");
        newCompanyDTO.setCity("Klaipeda");
        newCompanyDTO.setStreet("Gedimino pr.");
        newCompanyDTO.setHouseNumber("125");
        newCompanyDTO.setPhoneNumber("+370 626 62626");
        newCompanyDTO.setEmailAddress("takas@mail.com");

        companyService.saveCompanyData(newCompanyDTO);

        assertEquals(3, companyRepository.findAll().size());
    }

    @Test
    public void getCompaniesTest() {

        List<CompanyDTO> companies = companyService.getCompanies(pageable).stream().toList();

        assertEquals(2, companies.size());
        assertEquals("MB Kankorezis", companies.get(0).getCompanyName());
        assertEquals("UAB 'Kita planeta'", companies.get(1).getCompanyName());
    }

    @Test
    public void searchForCompany_CompanyIdNameCountryCityProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCompanyID(123456789L);
        companyDTO.setCompanyName("MB Kankorezis");
        companyDTO.setCountry("Lithuania");
        companyDTO.setCity("Kaunas");

        assertEquals(1, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
        assertEquals(123456789L, companyService.searchForCompany(pageable,companyDTO).iterator().next().getCompanyID());
    }

    @Test
    public void searchForCompany_CompanyNameCountryCityProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("MB Kankorezis");
        companyDTO.setCountry("Lithuania");
        companyDTO.setCity("Kaunas");

        assertEquals(1, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
        assertEquals(123456789L, companyService.searchForCompany(pageable,companyDTO).iterator().next().getCompanyID());
    }

    @Test
    public void searchForCompany_CompanyCountryCityProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCountry("Lithuania");
        companyDTO.setCity("Kaunas");

        assertEquals(1, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
        assertEquals(123456789L, companyService.searchForCompany(pageable,companyDTO).iterator().next().getCompanyID());
    }

    @Test
    public void searchForCompany_CompanyIdProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCompanyID(123456789L);

        assertEquals(1, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
        assertEquals(123456789L, companyService.searchForCompany(pageable,companyDTO).iterator().next().getCompanyID());
    }

    @Test
    public void searchForCompany_CompanyNameProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("MB Kankorezis");

        assertEquals(1, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
        assertEquals(123456789L, companyService.searchForCompany(pageable,companyDTO).iterator().next().getCompanyID());
    }

    @Test
    public void searchForCompany_CompanyCountryProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCountry("Lithuania");

        List<CompanyDTO> companyDTOS = companyService.searchForCompany(pageable, companyDTO).stream().toList();

        assertEquals(2, companyDTOS.size());
        assertEquals(123456789L, companyDTOS.get(0).getCompanyID());
        assertEquals(987654321L, companyDTOS.get(1).getCompanyID());
    }

    @Test
    public void searchForCompany_CompanyCityProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCity("Kaunas");

        assertEquals(1, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
        assertEquals(123456789L, companyService.searchForCompany(pageable,companyDTO).iterator().next().getCompanyID());
    }

    @Test
    public void searchForCompany_NullParametersProvided(){
        companyDTO = new CompanyDTO();

        assertEquals(0, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
    }

    @Test
    public void searchForCompany_EmptyParametersProvided(){
        companyDTO = new CompanyDTO();
        companyDTO.setCompanyName("");
        companyDTO.setCountry("");
        companyDTO.setCity("");

        assertEquals(2, companyService.searchForCompany(pageable, companyDTO).getNumberOfElements());
    }

    @Test
    public void getCompanyDTOByID(){

        assertNotNull(companyService.getCompanyDTOById(123456789L));
        assertEquals("MB Kankorezis", companyService.getCompanyById(123456789L).getCompanyName());
        assertEquals(CompanyType.SUPPLIER, companyService.getCompanyById(123456789L).getCompanyType());
    }

    @Test
    public void updateCompany(){

        assertEquals("MB Kankorezis", companyService.getCompanyById(123456789L).getCompanyName());

        CompanyDTO newCompanyDTO = new CompanyDTO();
        newCompanyDTO.setCompanyID(123456789L);
        newCompanyDTO.setCompanyName("AB 'Saules takas'");
        newCompanyDTO.setCountry("Lithuania");
        newCompanyDTO.setCity("Alytus");
        newCompanyDTO.setStreet("Gedimino pr.");
        newCompanyDTO.setHouseNumber("125");
        newCompanyDTO.setFlatNumber("47");
        newCompanyDTO.setPhoneNumber("+370 600 60006");
        newCompanyDTO.setEmailAddress("takas@mail.com");

        companyService.updateCompany(123456789L, newCompanyDTO);

        assertEquals("AB 'Saules takas'", companyService.getCompanyById(123456789L).getCompanyName());
        assertEquals("Alytus", companyService.getCompanyById(123456789L).getRegistrationAddress().getCity());
        assertEquals("+370 600 60006", companyService.getCompanyById(123456789L).getContactDetails().getPhoneNumber());
    }

    @Test
    public void getBusinessOwner(){

        Company newCompany = new Company();
        newCompany.setCompanyID(321321321L);
        newCompany.setCompanyName("UAB 'Musu Vieta'");
        newCompany.setCompanyType(CompanyType.OWNER);
        newCompany.setRegistrationAddress(new RegistrationAddress());
        newCompany.getRegistrationAddress().setCountry("Lithuania");
        newCompany.getRegistrationAddress().setCity("Kedainiai");
        newCompany.getRegistrationAddress().setStreet("K. Kerbedzio al.");
        newCompany.getRegistrationAddress().setHouseNumber("99");
        newCompany.setContactDetails(new ContactDetails());
        newCompany.getContactDetails().setPhoneNumber("+370 666 99955");
        newCompany.getContactDetails().setEmailAddress("mvieta@mail.com");

        companyRepository.save(newCompany);

        assertEquals(321321321L, companyService.getBusinessOwner().getCompanyID());
        assertEquals("UAB 'Musu Vieta'", companyService.getBusinessOwner().getCompanyName());
        assertEquals("Kedainiai", companyService.getBusinessOwner().getRegistrationAddress().getCity());
        assertEquals("mvieta@mail.com", companyService.getBusinessOwner().getContactDetails().getEmailAddress());
    }

    @Test
    public void getCompanyDTOByName(){

        assertEquals(123456789L, companyService.getCompanyDTOByName("MB Kankorezis").getCompanyID());
    }

    @Test
    public void getCompanyDTOByName_NonValidName(){
        assertNull(companyService.getCompanyDTOByName("MB Kastonas"));
    }

    @Test
    public void getSuppliers(){

        List<CompanyDTO> companyDTOList = companyService.getSuppliers(pageable).toList();

        assertEquals(1, companyDTOList.size());
        assertEquals(CompanyType.SUPPLIER, companyDTOList.get(0).getCompanyType());
    }

    @Test
    public void getCompanyTypes_Test(){

        assertEquals(2, companyPropertiesService.getCompanyTypes().size());
        assertEquals(CompanyType.CUSTOMER, companyPropertiesService.getCompanyTypes().get(0));
        assertEquals(CompanyType.SUPPLIER, companyPropertiesService.getCompanyTypes().get(1));
    }
}
