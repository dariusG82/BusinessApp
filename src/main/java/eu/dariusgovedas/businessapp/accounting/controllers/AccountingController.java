package eu.dariusgovedas.businessapp.accounting.controllers;

import eu.dariusgovedas.businessapp.accounting.services.AccountingService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Getter
@AllArgsConstructor
@RequestMapping
public class AccountingController {

    private AccountingService accountingService;

    @GetMapping("/private/finance/showOpenOrders")
    public String openPaymentForm(Model model, Pageable pageable){

        model.addAttribute("orders", accountingService.getAllOpenOrders(pageable));

        return "finance/paymentForm";
    }

    @PostMapping("/private/finance/makePayment/{orderID}")
    public String makePayment(@PathVariable Long orderID){

        accountingService.makePayment(orderID);

        return "redirect:/private/finance/showOpenOrders";
    }
}
