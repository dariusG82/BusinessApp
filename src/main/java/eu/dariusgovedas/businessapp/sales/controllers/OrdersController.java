package eu.dariusgovedas.businessapp.sales.controllers;

import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
import eu.dariusgovedas.businessapp.clients.service.ClientService;
import eu.dariusgovedas.businessapp.items.entities.ItemDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderLineDTO;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import eu.dariusgovedas.businessapp.sales.services.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
public class OrdersController {

    private ServletContext servletContext;
    private ClientService clientService;
    private OrdersService ordersService;

    @GetMapping("/private/sales/purchaseOrder")
    public String openPurchaseOrderForm(Model model){

        model.addAttribute("supplier", new ClientDTO());
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("item", new ItemDTO());
        model.addAttribute("results", Collections.emptyList());

        return "purchaseForm";
    }

    @GetMapping("/private/sales/findSupplier")
    public String getSupplier(Model model, Pageable pageable, ClientDTO clientDTO){

        model.addAttribute("supplier", new ClientDTO());

        model.addAttribute("results", clientService.searchForClient(pageable, clientDTO));

        return "purchaseForm";
    }

    @GetMapping("/private/sales/createPO/{id}")
    public String createNewPurchaseOrder(@PathVariable Long id, Model model){

        model.addAttribute("product", new ItemDTO());
        model.addAttribute("orderLines", Collections.emptyList());

        OrderDTO orderDTO = ordersService.createNewOrder(id, OrderType.PURCHASE);

        model.addAttribute("order", orderDTO);

        servletContext.setAttribute("orderDTO", orderDTO);

        return "orderForm";
    }


    @GetMapping("/private/warehouse/findProduct")
    public String createNewOrderLine(Model model, ItemDTO itemDTO, OrderDTO orderDTO){

        model.addAttribute("product", new ItemDTO());
        model.addAttribute("orderLines", Collections.emptyList());
        model.addAttribute("result", ordersService.getNewOrderLine(itemDTO.getItemNumber(), orderDTO));

        return "orderForm";
    }

    @GetMapping("/private/sales/updateQuantity/{number}")
    public String updateOrderLinePrice(@PathVariable("number") Long number, Model model, OrderLineDTO lineDTO){

        OrderDTO order = (OrderDTO) servletContext.getAttribute("orderDTO");
        Long orderNr = order.getOrderNumber();

        model.addAttribute("product", new ItemDTO());
        model.addAttribute("orderLines", Collections.emptyList());
        model.addAttribute("result", ordersService.updateOrderLine(orderNr, number, lineDTO));

        return "orderForm";
    }

    @PostMapping("/private/sales/saveOrderLine/{number}/{quantity}")
    public String saveOrderLine(@PathVariable Long number, @PathVariable Long quantity){


        OrderDTO order = (OrderDTO) servletContext.getAttribute("orderDTO");
        ordersService.saveNewOrderLine(order, number, quantity);
        OrderDTO updatedOrderDTO = ordersService.getUpdatedOrderDTO(order);

        servletContext.removeAttribute("orderDTO");
        servletContext.setAttribute("orderDTO", updatedOrderDTO);

        return "redirect:/private/orderForm";
    }

    @GetMapping("/private/orderForm")
    public String openOrder(Model model){

        model.addAttribute("product", new ItemDTO());

        OrderDTO order = (OrderDTO) servletContext.getAttribute("orderDTO");
        List<OrderLineDTO> orderLineList = ordersService.getOrderLines(order);

        model.addAttribute("orderLines", orderLineList);

        return "orderForm";
    }
}
