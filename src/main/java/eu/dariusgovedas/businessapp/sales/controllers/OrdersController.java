package eu.dariusgovedas.businessapp.sales.controllers;

import eu.dariusgovedas.businessapp.companies.entities.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import eu.dariusgovedas.businessapp.common.PDFExporter;
import eu.dariusgovedas.businessapp.items.entities.ItemDTO;
import eu.dariusgovedas.businessapp.sales.entities.InvoiceDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderLineDTO;
import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping
@AllArgsConstructor
public class OrdersController {

    private ServletContext servletContext;
    private CompanyService companyService;
    private OrdersService ordersService;

    @GetMapping("/private/sales/purchaseOrder")
    public String openPurchaseOrderForm(Model model) {

        model.addAttribute("supplier", new CompanyDTO());
        model.addAttribute("order", new OrderDTO());
        model.addAttribute("item", new ItemDTO());
        model.addAttribute("results", Collections.emptyList());

        return "sales/purchaseForm";
    }

    @GetMapping("/private/sales/findSupplier")
    public String getSupplier(Model model, Pageable pageable, CompanyDTO companyDTO) {

        model.addAttribute("supplier", new CompanyDTO());

        model.addAttribute("results", companyService.searchForCompany(pageable, companyDTO));

        return "sales/purchaseForm";
    }

    @GetMapping("/private/sales/createPO/{id}")
    public String createNewPurchaseOrder(@PathVariable Long id, Model model) {

        model.addAttribute("product", new ItemDTO());
        model.addAttribute("orderLines", Collections.emptyList());

        OrderDTO orderDTO = ordersService.createNewOrder(id, OrderType.PURCHASE);

        model.addAttribute("order", orderDTO);

        servletContext.setAttribute("orderDTO", orderDTO);
        servletContext.setAttribute("status", orderDTO.getStatus().toString());

        return "sales/orderForm";
    }


    @GetMapping("/private/warehouse/findProduct")
    public String createNewOrderLine(Model model, ItemDTO itemDTO) {

        OrderDTO orderDTO = (OrderDTO) servletContext.getAttribute("orderDTO");

        model.addAttribute("product", new ItemDTO());
        model.addAttribute("orderLines", Collections.emptyList());
        model.addAttribute("result", ordersService.getNewOrderLine(itemDTO.getItemNumber(), orderDTO));

        return "sales/orderForm";
    }

    @GetMapping("/private/sales/updateQuantity/{number}")
    public String updateOrderLinePrice(@PathVariable("number") Long number, Model model, OrderLineDTO lineDTO) {

        OrderDTO orderDTO = (OrderDTO) servletContext.getAttribute("orderDTO");
        Long orderNr = orderDTO.getOrderNumber();

        model.addAttribute("product", new ItemDTO());
        model.addAttribute("orderLines", Collections.emptyList());
        model.addAttribute("result", ordersService.updateOrderLine(orderNr, number, lineDTO));

        return "sales/orderForm";
    }

    @PostMapping("/private/sales/saveOrderLine/{number}/{quantity}")
    public String saveOrderLine(@PathVariable Long number, @PathVariable Long quantity) {


        OrderDTO order = (OrderDTO) servletContext.getAttribute("orderDTO");
        ordersService.saveNewOrderLine(order, number, quantity);
        OrderDTO updatedOrderDTO = ordersService.getUpdatedOrderDTO(order);

        servletContext.removeAttribute("orderDTO");
        servletContext.setAttribute("orderDTO", updatedOrderDTO);

        return "redirect:/private/orderForm";
    }

    @GetMapping("/private/orderForm")
    public String openOrder(Model model) {

        model.addAttribute("product", new ItemDTO());

        OrderDTO order = (OrderDTO) servletContext.getAttribute("orderDTO");
        List<OrderLineDTO> orderLineList = ordersService.getOrderLines(order);

        model.addAttribute("orderLines", orderLineList);

        return "sales/orderForm";
    }

    @GetMapping("/private/sales/finishOrder")
    public String finishOrder() {
        OrderDTO orderDTO = (OrderDTO) servletContext.getAttribute("orderDTO");

        ordersService.saveInvoicedOrder(orderDTO);

        orderDTO.setStatus(OrderStatus.INVOICED);

        servletContext.removeAttribute("orderDTO");
        servletContext.setAttribute("orderDTO", orderDTO);

        servletContext.removeAttribute("status");
        servletContext.setAttribute("status", orderDTO.getStatus().toString());

        return "redirect:/private/orderForm";
    }

    @GetMapping("/private/sales/printOrder")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String currantDateTime = LocalDate.now().toString();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=invoice_" + currantDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        OrderDTO orderDTO = (OrderDTO) servletContext.getAttribute("orderDTO");

        InvoiceDTO invoiceDTO = ordersService.getInvoiceDTO(orderDTO);

        PDFExporter exporter = new PDFExporter(invoiceDTO);
        exporter.export(response);
    }
}
