package eu.dariusgovedas.businessapp.companies.controller;

import eu.dariusgovedas.businessapp.companies.entities.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.service.CompanyPropertiesService;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class CompanyController {

    private CompanyService companyService;
    private CompanyPropertiesService companyPropertiesService;

    @GetMapping("/private/companies/createCompany")
    public String openCompanyForm(Model model){

        model.addAttribute("company", new CompanyDTO());
        model.addAttribute("companyTypeOptions", companyPropertiesService.getCompanyTypes());
        return "companyForm";
    }

    @PostMapping("/private/companies/createCompany")
    public String saveCompanyData(CompanyDTO companyDTO){

        companyService.saveCompanyData(companyDTO);

        return "redirect:/private/companies";
    }

    @GetMapping("/private/companies/showCompanies")
    public String showCompanies(Pageable pageable, Model model){

        model.addAttribute("company", new CompanyDTO());

        Page<CompanyDTO> companyDTOS = companyService.getCompanies(pageable);

        model.addAttribute("companies",companyDTOS );

        return "companies";
    }

    @GetMapping("/private/companies/findCompany")
    public String getSearchResults(Model model, Pageable pageable, CompanyDTO companyDTO){

        model.addAttribute("company", new CompanyDTO());

        model.addAttribute("companies", companyService.searchForCompany(pageable, companyDTO));

        return "companies";
    }

    @GetMapping("/private/companies/edit/{id}")
    public String openClientEditForm(@PathVariable Long id, Model model){

        model.addAttribute("company", companyService.getCompanyDTOById(id));
        model.addAttribute("companyTypeOptions", companyPropertiesService.getCompanyTypes());

        return "companyForm";
    }

    @PostMapping("/private/companies/edit/{id}")
    public String updateClientData(@PathVariable Long id, CompanyDTO companyDTO){

        companyService.updateClient(id, companyDTO);

        return "redirect:/private/companies/showCompanies";
    }
}
