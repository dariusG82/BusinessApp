package eu.dariusgovedas.businessapp.companies.entities;

import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CompanyDTO {

    private Long companyID;
    private String companyName;
    private CompanyType companyType;
    private String country;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String phoneNumber;
    private String emailAddress;

    private String streetAddress;

    public String getStreetAddress() {
        String houseAddress = street + " " + houseNumber;
        String flatAddress = houseAddress + "-" + flatNumber;

        return flatNumber == null || flatNumber.equals("") ? houseAddress : flatAddress;
    }
}
