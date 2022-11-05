package eu.dariusgovedas.businessapp.controllers;

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
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class CompanyControllerTest {

    private static MockHttpServletRequest request;

    @Autowired
    private CompanyDTO customerCompanyDTO;
    @Autowired
    private Company company;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private CompanyService companyServiceMock;
    @Mock
    private CompanyPropertiesService companyPropertiesServiceMock;

    @Autowired
    private CompanyRepository companyRepository;

    public static Pageable pageable;


    @BeforeAll
    public static void setup() {
        pageable = Pageable.ofSize(5);

        request = new MockHttpServletRequest();
        request.setParameter("companyId", "123456789");
        request.setParameter("companyName", "MB Kankorezis");
        request.setParameter("companyType", CompanyType.SUPPLIER.getValue());
        request.setParameter("country", "Lithuania");
        request.setParameter("city", "Kaunas");
        request.setParameter("street", "V. Kudirkos g.");
        request.setParameter("houseNumber", "35a");
        request.setParameter("flatNumber", "25");
        request.setParameter("phoneNumber", "+370 626 70088");
        request.setParameter("emailAddress", "kankorezis@mail.com");
    }

    @BeforeEach
    void beforeEach() {
        customerCompanyDTO.setCompanyID(987654321L);
        customerCompanyDTO.setCompanyName("UAB 'Kita planeta'");
        customerCompanyDTO.setCompanyType(CompanyType.CUSTOMER);
        customerCompanyDTO.setCountry("Lithuania");
        customerCompanyDTO.setCity("Vilnius");
        customerCompanyDTO.setStreet("A. Brazausko al.");
        customerCompanyDTO.setHouseNumber("22");
        customerCompanyDTO.setPhoneNumber("+370 677 89955");
        customerCompanyDTO.setEmailAddress("planeta@mail.com");

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
    void afterEach(){
        companyRepository.delete(company);
    }

    @Test
    public void getCompanyForm_HttpRequest() throws Exception {

        List<CompanyType> companyTypes = new ArrayList<>();
        companyTypes.add(CompanyType.SUPPLIER);
        companyTypes.add(CompanyType.CUSTOMER);

        when(companyPropertiesServiceMock.getCompanyTypes()).thenReturn(companyTypes);

        assertIterableEquals(companyTypes, companyPropertiesServiceMock.getCompanyTypes());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/private/companies/createCompany"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "companies/companyForm");
    }

    @Test
    public void saveCompanyData_HttpRequest() throws Exception {

        MvcResult result = this.mockMvc.perform(post("/private/companies/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("companyID", request.getParameterValues("companyId"))
                        .param("companyName", request.getParameterValues("companyName"))
                        .param("companyType", request.getParameterValues("companyType"))
                        .param("country", request.getParameterValues("country"))
                        .param("city", request.getParameterValues("city"))
                        .param("street", request.getParameterValues("street"))
                        .param("houseNumber", request.getParameterValues("houseNumber"))
                        .param("flatNumber", request.getParameterValues("flatNumber"))
                        .param("phoneNumber", request.getParameterValues("phoneNumber"))
                        .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "redirect:/private/companies");
    }

    @Test
    public void showCompanies_HttpRequest() throws Exception {

        when(companyServiceMock.getCompanies(pageable)).thenReturn(new PageImpl<>(List.of(customerCompanyDTO),pageable,1));

        assertIterableEquals(List.of(customerCompanyDTO), companyServiceMock.getCompanies(pageable));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/private/companies/showCompanies"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "companies/companies");
    }

    @Test
    public void getSearchResults_HttpRequest() throws Exception{

        when(companyServiceMock.searchForCompany(pageable, customerCompanyDTO))
                .thenReturn(new PageImpl<>(List.of(customerCompanyDTO),pageable,1));

        assertIterableEquals(List.of(customerCompanyDTO), companyServiceMock.searchForCompany(pageable, customerCompanyDTO));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/private/companies/findCompany"))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "companies/companies");
    }

    @Test
    public void openClientEditForm_HttpRequest() throws Exception {

        Optional<Company> companyOptional = companyRepository.findById(987654321L);
        assertTrue(companyOptional.isPresent());

        when(companyServiceMock.getCompanyDTOById(987654321L)).thenReturn(customerCompanyDTO);

        assertEquals(customerCompanyDTO, companyServiceMock.getCompanyDTOById(987654321L));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/private/companies/edit/{id}", 987654321L))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "companies/companyForm");
    }

    @Test
    public void openNonExistingClientEditForm_HttpRequest() throws Exception{

        Optional<Company> companyOptional = companyRepository.findById(654321L);
        assertFalse(companyOptional.isPresent());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/private/companies/edit/{id}", 654321L))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error/404");
    }

    @Test
    public void updateClientData_HttpRequest() throws Exception {

        Optional<Company> companyOptional = companyRepository.findById(987654321L);
        assertTrue(companyOptional.isPresent());

        MvcResult result = this.mockMvc.perform(
                post("/private/companies/edit/{id}", 987654321L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("companyName", request.getParameterValues("companyName"))
                        .param("companyType", request.getParameterValues("companyType"))
                        .param("country", request.getParameterValues("country"))
                        .param("city", request.getParameterValues("city"))
                        .param("street", request.getParameterValues("street"))
                        .param("houseNumber", request.getParameterValues("houseNumber"))
                        .param("flatNumber", request.getParameterValues("flatNumber"))
                        .param("phoneNumber", request.getParameterValues("phoneNumber"))
                        .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().is3xxRedirection())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "redirect:/private/companies/showCompanies");
    }

    @Test
    public void updateNonExistingClientData_HttpRequest() throws Exception {
        Optional<Company> companyOptional = companyRepository.findById(7654321L);
        assertFalse(companyOptional.isPresent());

        MvcResult result = this.mockMvc.perform(
                        post("/private/companies/edit/{id}", 7654321L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("companyName", request.getParameterValues("companyName"))
                                .param("companyType", request.getParameterValues("companyType"))
                                .param("country", request.getParameterValues("country"))
                                .param("city", request.getParameterValues("city"))
                                .param("street", request.getParameterValues("street"))
                                .param("houseNumber", request.getParameterValues("houseNumber"))
                                .param("flatNumber", request.getParameterValues("flatNumber"))
                                .param("phoneNumber", request.getParameterValues("phoneNumber"))
                                .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().isOk())
                .andReturn();

        ModelAndView mav = result.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "error/404");
    }
}
