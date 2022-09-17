package eu.dariusgovedas.businessapp.clients.entities;

import eu.dariusgovedas.businessapp.clients.enums.ClientType;
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
public class Client {

    @Id
    private Long businessID;

    private String businessName;

    @Enumerated(value = EnumType.STRING)
    private ClientType clientType;

    @OneToOne(cascade = CascadeType.ALL)
    private RegistrationAddress registrationAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private ContactDetails contactDetails;
}
