package eu.dariusgovedas.businessapp.clients.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    private Long businessID;

    @OneToOne(cascade = CascadeType.ALL)
    private RegistrationAddress registrationAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private ContactDetails contactDetails;
}
