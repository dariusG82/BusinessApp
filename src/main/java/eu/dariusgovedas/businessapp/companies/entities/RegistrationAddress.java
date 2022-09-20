package eu.dariusgovedas.businessapp.companies.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationAddress {

    @Id
    @GeneratedValue
    private Long id;

    private String country;

    private String city;

    private String street;

    private String houseNumber;

    private String flatNumber;

    @OneToOne(mappedBy = "registrationAddress")
    private Company company;
}
