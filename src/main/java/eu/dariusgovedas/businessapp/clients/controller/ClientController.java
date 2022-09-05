package eu.dariusgovedas.businessapp.clients.controller;

import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
import eu.dariusgovedas.businessapp.clients.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class ClientController {

    private ClientService clientService;

    @GetMapping("/private/clients/createClient")
    public String openClientForm(Model model){

        model.addAttribute("client", new ClientDTO());
        return "clientForm";
    }

    @PostMapping("/private/clients/createClient")
    public String saveClientData(ClientDTO clientDTO){

        clientService.saveClientData(clientDTO);

        return "redirect:/private/accounting";
    }

    @GetMapping("/private/clients/showClients")
    public String showClients(Pageable pageable, Model model){

        model.addAttribute("client", new ClientDTO());

        Page<ClientDTO> clientDTOS = clientService.getClients(pageable);

        model.addAttribute("clients",clientDTOS );

        return "clients";
    }

    @GetMapping("/private/clients/findClient")
    public String getSearchResults(Model model, Pageable pageable, ClientDTO clientDTO){

        model.addAttribute("client", new ClientDTO());

        model.addAttribute("clients", clientService.searchForClient(pageable, clientDTO));

        return "clients";
    }

    @GetMapping("/private/clients/edit/{id}")
    public String openClientEditForm(@PathVariable Long id, Model model){

        model.addAttribute("client", clientService.getClientById(id));

        return "clientForm";
    }

    @PostMapping("/private/clients/edit/{id}")
    public String updateClientData(@PathVariable Long id, ClientDTO clientDTO){

        clientService.updateClient(clientDTO);

        return "redirect:/private/clients/showClients";
    }
}
