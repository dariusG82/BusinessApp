package eu.dariusgovedas.businessapp.navigation.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class NavigationController {

    @GetMapping("/public/home")
    public String openStartPage(){

        return "startPage";
    }

    @GetMapping("/private/admin")
    public String openAdminPage(){

        return "admin/adminPage";
    }

    @GetMapping("/private/accounting")
    public String openAccountingPage(){

        return "finance/accountingPage";
    }

    @GetMapping("/private/companies")
    public String openClientsPage(){

        return "companies/companiesPage";
    }

    @GetMapping("/private/sales")
    public String openSalesPage(){

        return "sales/salesmanPage";
    }

    @GetMapping("/private/warehouse")
    public String openWarehousePage(){

        return "warehouse/warehousePage";
    }
}
