package eu.dariusgovedas.businessapp.clients.service;

import eu.dariusgovedas.businessapp.clients.entities.Client;
import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
import eu.dariusgovedas.businessapp.clients.entities.ContactDetails;
import eu.dariusgovedas.businessapp.clients.entities.RegistrationAddress;
import eu.dariusgovedas.businessapp.clients.enums.ClientType;
import eu.dariusgovedas.businessapp.clients.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientService {

    private ClientRepository clientRepository;

    @Transactional
    public void saveClientData(ClientDTO clientDTO) {
        Client client = getClientFromClientDTO(clientDTO);

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

    public Page<ClientDTO> getClients(Pageable pageable) {
        List<Client> clients = clientRepository.findAll();

        List<ClientDTO> clientDTOS = new ArrayList<>();

        for (Client client : clients) {
            clientDTOS.add(getClientDTOFromClient(client));
        }

        return new PageImpl<>(clientDTOS, pageable, clientDTOS.size());
    }

    private Client getClientFromClientDTO(ClientDTO clientDTO) {
        Client client = new Client();
        client.setBusinessID(clientDTO.getBusinessID());
        client.setBusinessName(clientDTO.getBusinessName());

        RegistrationAddress registrationAddress = getRegistrationAddress(clientDTO);

        ContactDetails contactDetails = getContactDetails(clientDTO);

        client.setRegistrationAddress(registrationAddress);
        client.setContactDetails(contactDetails);

        return client;
    }

    private ClientDTO getClientDTOFromClient(Client client) {
        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setBusinessID(client.getBusinessID());
        clientDTO.setBusinessName(client.getBusinessName());
        clientDTO.setCountry(client.getRegistrationAddress().getCountry());
        clientDTO.setCity(client.getRegistrationAddress().getCity());
        clientDTO.setStreet(client.getRegistrationAddress().getStreet());
        clientDTO.setHouseNumber(client.getRegistrationAddress().getHouseNumber());
        clientDTO.setFlatNumber(client.getRegistrationAddress().getFlatNumber());
        clientDTO.setPhoneNumber(client.getContactDetails().getPhoneNumber());
        clientDTO.setEmailAddress(client.getContactDetails().getEmailAddress());

        return clientDTO;
    }

    public Page<ClientDTO> searchForClient(Pageable pageable, ClientDTO clientDTO) {
        List<ClientDTO> clientDTOS = new ArrayList<>();

        if (clientDTO.getBusinessID() != null) {
            Client client = clientRepository.findByBusinessID(clientDTO.getBusinessID());
            if(client == null){
                return new PageImpl<>(clientDTOS, pageable, 0);
            }
            if (checkBusinessName(client, clientDTO) && checkCountry(client, clientDTO) && checkCity(client, clientDTO)) {
                clientDTOS.add(getClientDTOFromClient(client));
                return new PageImpl<>(clientDTOS, pageable, clientDTOS.size());
            }
        }

        if (clientDTO.getBusinessName() != null) {
            List<Client> clients = clientRepository.findByBusinessNameContainingIgnoreCase(clientDTO.getBusinessName());
            for (Client client : clients) {
                if (checkCountry(client, clientDTO) && checkCity(client, clientDTO)) {
                    clientDTOS.add(getClientDTOFromClient(client));
                }
            }
            return new PageImpl<>(clientDTOS, pageable, clientDTOS.size());
        }

        if (clientDTO.getCountry() != null) {
            List<Client> clients = clientRepository.findByCountry(clientDTO.getCountry().toUpperCase());
            for (Client client : clients) {
                if (checkCity(client, clientDTO)) {
                    clientDTOS.add(getClientDTOFromClient(client));
                }
            }
            return new PageImpl<>(clientDTOS, pageable, clientDTOS.size());
        }

        if (clientDTO.getCity() != null) {
            List<Client> clients = clientRepository.findByCity(clientDTO.getCity().toUpperCase());
            for (Client client : clients) {
                clientDTOS.add(getClientDTOFromClient(client));
            }
            return new PageImpl<>(clientDTOS, pageable, clientDTOS.size());
        }

        return new PageImpl<>(clientDTOS, pageable, 0);
    }

    private boolean checkBusinessName(Client client, ClientDTO clientDTO) {
        if (clientDTO.getBusinessName() == null) {
            return true;
        } else {
            return client.getBusinessName().toUpperCase().contains(clientDTO.getBusinessName().toUpperCase());
        }
    }

    private boolean checkCountry(Client client, ClientDTO clientDTO) {
        if (clientDTO.getCountry() == null) {
            return true;
        } else {
            return client.getRegistrationAddress().getCountry().toUpperCase().contains(clientDTO.getCountry().toUpperCase());
        }
    }

    private boolean checkCity(Client client, ClientDTO clientDTO) {
        if (clientDTO.getCity() == null) {
            return true;
        } else {
            return client.getRegistrationAddress().getCity().toUpperCase().contains(clientDTO.getCity().toUpperCase());
        }
    }

    public Client getClientById(Long id) {
        return clientRepository.findByBusinessID(id);
    }

    public ClientDTO getClientDTOById(Long id) {
        Client client = getClientById(id);

        return getClientDTOFromClient(client);
    }

    @Transactional
    public void updateClient(Long id, ClientDTO clientDTO) {
        Client client = clientRepository.findByBusinessID(id);
        client.setBusinessName(clientDTO.getBusinessName());
        client.getRegistrationAddress().setCountry(clientDTO.getCountry());
        client.getRegistrationAddress().setCity(clientDTO.getCity());
        client.getRegistrationAddress().setStreet(clientDTO.getStreet());
        client.getRegistrationAddress().setHouseNumber(clientDTO.getHouseNumber());
        client.getRegistrationAddress().setFlatNumber(clientDTO.getFlatNumber());
        client.getContactDetails().setPhoneNumber(clientDTO.getPhoneNumber());
        client.getContactDetails().setEmailAddress(clientDTO.getEmailAddress());
    }

    public Client getBusinessOwner() {
        return clientRepository.findByClientType(ClientType.OWNER);
    }

    public ClientDTO getClientDTOByName(String clientName) {
        Client client = clientRepository.findByBusinessName(clientName);
        return getClientDTOFromClient(client);
    }
}
