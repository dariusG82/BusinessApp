package eu.dariusgovedas.businessapp.clients.controller;

import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
import eu.dariusgovedas.businessapp.clients.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class ClientController {

    private ClientService clientService;

    @GetMapping("/private/accounting/createClient")
    public String openClientForm(Model model){

        model.addAttribute("client", new ClientDTO());
        return "clientForm";
    }

    @PostMapping("/private/accounting/createClient")
    public String saveClientData(ClientDTO clientDTO){

        clientService.saveClientData(clientDTO);

        return "redirect:/private/accounting";
    }

    @GetMapping("/private/accounting/showClients")
    public String showClients(Pageable pageable, Model model){

        model.addAttribute("client", new ClientDTO());

        Page<ClientDTO> clientDTOS = clientService.getClients(pageable);

        model.addAttribute("clients",clientDTOS );

        return "clients";
    }

    @GetMapping("/private/accounting/findClient")
    public String getSearchResults(Model model, Pageable pageable, ClientDTO clientDTO){

        model.addAttribute("client", new ClientDTO());

        model.addAttribute("clients", clientService.searchForClient(pageable, clientDTO));

        return "clients";
    }
}
