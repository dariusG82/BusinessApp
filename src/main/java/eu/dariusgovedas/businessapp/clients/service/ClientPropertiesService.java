package eu.dariusgovedas.businessapp.clients.service;

import eu.dariusgovedas.businessapp.clients.enums.ClientType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ClientPropertiesService {
    public List<ClientType> getClientTypes() {
        List<ClientType> clientTypes = new ArrayList<>();

        clientTypes.add(ClientType.CUSTOMER);
        clientTypes.add(ClientType.SUPPLIER);

        return clientTypes;
    }
}
