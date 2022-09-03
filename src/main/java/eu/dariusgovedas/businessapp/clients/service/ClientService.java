package eu.dariusgovedas.businessapp.clients.service;

import eu.dariusgovedas.businessapp.clients.entities.Client;
import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
import eu.dariusgovedas.businessapp.clients.entities.ContactDetails;
import eu.dariusgovedas.businessapp.clients.entities.RegistrationAddress;
import eu.dariusgovedas.businessapp.clients.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepository clientRepository;

    @Transactional
    public void saveClientData(ClientDTO clientDTO) {
        Client client = new Client();
        client.setBusinessID(clientDTO.getBusinessID());
        client.setBusinessName(clientDTO.getBusinessName());

        RegistrationAddress registrationAddress = getRegistrationAddress(clientDTO);

        ContactDetails contactDetails = getContactDetails(clientDTO);

        client.setRegistrationAddress(registrationAddress);
        client.setContactDetails(contactDetails);

        clientRepository.save(client);
    }

    private RegistrationAddress getRegistrationAddress(ClientDTO clientDTO) {
        RegistrationAddress registrationAddress = new RegistrationAddress();
        registrationAddress.setCountry(clientDTO.getCountry());
        registrationAddress.setCity(clientDTO.getCity());
        registrationAddress.setStreet(clientDTO.getStreet());
        registrationAddress.setHouseNumber(clientDTO.getHouseNumber());
        registrationAddress.setFlatNumber(clientDTO.getFlatNumber());
        return registrationAddress;
    }

    private ContactDetails getContactDetails(ClientDTO clientDTO) {
        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setPhoneNumber(clientDTO.getPhoneNumber());
        contactDetails.setEmailAddress(clientDTO.getEmailAddress());
        return contactDetails;
    }
}
