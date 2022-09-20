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
public class ContactDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String phoneNumber;

    private String emailAddress;

    @OneToOne(mappedBy = "contactDetails")
    private Company company;
}
