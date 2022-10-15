package eu.dariusgovedas.businessapp.items.controller;

import eu.dariusgovedas.businessapp.items.entities.dto.ItemPropertiesDTO;
import eu.dariusgovedas.businessapp.items.service.ItemPropertiesService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@AllArgsConstructor
public class ItemPropertiesController {

    private ItemPropertiesService itemPropertiesService;

    @GetMapping("/private/editItemData")
    public String openDataForm(Model model){

        model.addAttribute("itemData", new ItemPropertiesDTO());

        return "admin/editItemData";
    }

    @PostMapping("/private/editItemData/saveCategory")
    public String saveNewCategory(ItemPropertiesDTO propertiesDTO){

        itemPropertiesService.saveNewCategory(propertiesDTO);

        return "redirect:/private/editItemData";
    }
}
