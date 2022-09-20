package eu.dariusgovedas.businessapp.companies.entities;

import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    private Long companyID;

    private String companyName;

    @Enumerated(value = EnumType.STRING)
    private CompanyType companyType;

    @OneToOne(cascade = CascadeType.ALL)
    private RegistrationAddress registrationAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private ContactDetails contactDetails;
}
