package eu.dariusgovedas.businessapp.common.supplier.controllers;

import eu.dariusgovedas.businessapp.common.supplier.services.SupplierService;
import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.service.ItemPropertiesService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;
    private final ItemPropertiesService itemPropertiesService;

    @GetMapping("/private/testData/showSuppliers")
    public String showSuppliers(Model model, Pageable pageable){

        model.addAttribute("supplier", new Company());
        model.addAttribute("suppliersList", supplierService.getSuppliers(pageable));

        return "testData/suppliers";
    }

    @GetMapping("/private/testData/editStock/{id}")
    public String openSupplierStock(@PathVariable Long id, Model model, Pageable pageable){

        model.addAttribute("supplierID", id);
        model.addAttribute("stockList", supplierService.getSupplierStock(id, pageable));

        return "testData/stockPage";
    }

    @GetMapping("/private/testData/addSupplies/{id}")
    public String openAddSuppliesForm(@PathVariable Long id, Model model){

        model.addAttribute("supplierID", id);
        List<String> options = itemPropertiesService.getCategories();
        model.addAttribute("options", options);
        model.addAttribute("supplierItem", new ItemDTO());

        return "testData/addSuppliesForm";
    }

    @PostMapping("/private/testData/{id}/addSupplierItem")
    public String addSupplierItem(@PathVariable Long id, ItemDTO itemDTO){

        supplierService.addItem(id, itemDTO);

        return "redirect:/private/testData/showSuppliers";
    }
}
