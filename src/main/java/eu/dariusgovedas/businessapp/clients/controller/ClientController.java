package eu.dariusgovedas.businessapp.clients.controller;

import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
import eu.dariusgovedas.businessapp.clients.service.ClientService;
import lombok.AllArgsConstructor;
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
}
