package eu.dariusgovedas.businessapp.items.controller;

import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.service.ItemPropertiesService;
import eu.dariusgovedas.businessapp.items.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
public class ItemController {

    private ItemService itemService;
    private ItemPropertiesService itemPropertiesService;

    @GetMapping("/private/item/create")
    public String openItemForm(Model model) {

        model.addAttribute("item", new ItemDTO());

        List<String> options = itemPropertiesService.getCategories();
        model.addAttribute("options", options);

        return "warehouse/itemForm";
    }

    @PostMapping("/private/item/create")
    public String createItemCard(ItemDTO itemDTO) {

        itemService.createItem(itemDTO);

        return "redirect:/private/warehouse";
    }

    @GetMapping("/private/item/show")
    public String showWarehouseStock(Pageable pageable, Model model) {

        Page<ItemDTO> warehouseStock = itemService.getWarehouseStock(pageable);
        model.addAttribute("items", warehouseStock);

        return "warehouse/warehouseStock";
    }
}
