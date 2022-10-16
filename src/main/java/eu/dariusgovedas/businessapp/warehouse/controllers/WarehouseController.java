package eu.dariusgovedas.businessapp.warehouse.controllers;

import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderLineDTO;
import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.services.OrdersService;
import eu.dariusgovedas.businessapp.warehouse.services.WarehouseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class WarehouseController {

    private WarehouseService warehouseService;
    private OrdersService ordersService;

    private ServletContext servletContext;

    @GetMapping("/private/item/show")
    public String showWarehouseStock(Pageable pageable, Model model) {

        Page<ItemDTO> warehouseStock = warehouseService.getWarehouseStock(pageable);
        model.addAttribute("items", warehouseStock);

        return "warehouse/warehouseStock";
    }

    @GetMapping("/private/order/receive")
    public String openReceiveOrderForm(Pageable pageable, Model model){

        Page<OrderDTO> orderDTOS = ordersService.getOrdersByStatus(pageable, OrderStatus.CONFIRMED);
        List<OrderLineDTO> orderLineDTOS = new ArrayList<>();

        model.addAttribute("orders", orderDTOS);
        model.addAttribute("orderLines", orderLineDTOS);

        return "warehouse/receiveOrderPage";
    }

    @GetMapping("/private/warehouse/displayGoods/{orderNr}")
    public String displayOrderLines(@PathVariable Long orderNr,Pageable pageable, Model model){

        Page<OrderDTO> orderDTOS = ordersService.getOrdersByStatus(pageable, OrderStatus.CONFIRMED);
        OrderDTO orderDTO = ordersService.getOrderDTOByID(orderNr);

        List<OrderLineDTO> orderLineDTOS = ordersService.getOrderLines(orderDTO);

        model.addAttribute("orders", orderDTOS);
        model.addAttribute("orderLines", orderLineDTOS);

        servletContext.setAttribute("orderNumber", orderDTO.getOrderNumber());

        return "warehouse/receiveOrderPage";
    }

    @PostMapping("/private/warehouse/receiveGoods")
    public String receiveGoods(){

        Long orderNumber = (Long) servletContext.getAttribute("orderNumber");

        warehouseService.updateWarehouseStock(orderNumber);
        ordersService.updateOrderStatus(orderNumber, OrderStatus.FINISHED);

        servletContext.removeAttribute("orderNumber");

        return "redirect:/private/order/receive";
    }
}
