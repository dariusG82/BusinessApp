package eu.dariusgovedas.businessapp.items.controller;

import eu.dariusgovedas.businessapp.items.entities.Item;
import eu.dariusgovedas.businessapp.items.entities.ItemDTO;
import eu.dariusgovedas.businessapp.items.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;

    @GetMapping("/private/item/create")
    public String openItemForm(Model model){

        model.addAttribute("item", new ItemDTO());

        return "itemForm";
    }

    @PostMapping("/private/item/create")
    public String createItemCard(ItemDTO itemDTO){

        itemService.createItem(itemDTO);

        return "redirect:/private/warehouse";
    }
}
