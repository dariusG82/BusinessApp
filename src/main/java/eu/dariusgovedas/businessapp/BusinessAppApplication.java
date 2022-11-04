package eu.dariusgovedas.businessapp;

import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class BusinessAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(BusinessAppApplication.class, args);
    }

    @Bean
    @Scope(value = "prototype")
    CompanyDTO getCompanyDTO(){
        return new CompanyDTO();
    }

    @Bean
    @Scope(value = "prototype")
    Company getCompany(){
        return new Company();
    }

}
