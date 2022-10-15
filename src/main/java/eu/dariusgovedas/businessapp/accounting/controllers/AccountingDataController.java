package eu.dariusgovedas.businessapp.accounting.controllers;

import eu.dariusgovedas.businessapp.accounting.entities.dto.AccountDTO;
import eu.dariusgovedas.businessapp.accounting.entities.dto.BankDTO;
import eu.dariusgovedas.businessapp.accounting.services.BankService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class AccountingDataController {

    private BankService bankService;

    @GetMapping("/private/editAccountingData")
    public String openAccountingDataEditor(Model model){

        model.addAttribute("bankData", new BankDTO());
        model.addAttribute("accountData", new AccountDTO());
        model.addAttribute("bankList", bankService.getAllBanks());

        return "admin/editAccountingData";
    }

    @PostMapping("/private/addBankData")
    public String saveNewBank(BankDTO bankDTO){

        bankService.addBank(bankDTO);

        return "redirect:/private/editAccountingData";
    }

    @PostMapping("/private/addAccount")
    public String saveNewAccount(AccountDTO accountDTO){

        bankService.addAccount(accountDTO);

        return "redirect:/private/editAccountingData";
    }

}
