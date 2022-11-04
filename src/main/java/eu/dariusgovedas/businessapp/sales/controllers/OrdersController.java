package eu.dariusgovedas.businessapp.sales.controllers;

import eu.dariusgovedas.businessapp.common.PDFExporter;
import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
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

        companyDTO.setCompanyType(CompanyType.SUPPLIER);

        model.addAttribute("results", companyService.searchForCompany(pageable, companyDTO));

        return "sales/purchaseForm";
    }

    @GetMapping("/private/sales/createPO/{id}")
    public String createNewPurchaseOrder(@PathVariable Long id, Model model, Pageable pageable) {

        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setOrderQuantity(0L);
        model.addAttribute("product", itemDTO);
        model.addAttribute("orderLines", Collections.emptyList());

        OrderDTO orderDTO = ordersService.createNewOrder(id, OrderType.PURCHASE);

        servletContext.setAttribute("stock", ordersService.getSupplierStock(id, pageable));
        servletContext.setAttribute("supplierID", id);
        servletContext.setAttribute("orderLinesDTO", ordersService.getSupplierOrderLines(id, orderDTO.getOrderNumber()));
        servletContext.setAttribute("orderDTO", orderDTO);
        servletContext.setAttribute("status", orderDTO.getStatus().toString());

        return "sales/orderForm";
    }

    @GetMapping("/private/sales/updateQuantity/{number}")
    public String updateOrderLinePrice(@PathVariable("number") Long number, ItemDTO itemDTO, Model model) {

        OrderDTO orderDTO = (OrderDTO) servletContext.getAttribute("orderDTO");
        List<OrderLineDTO> orderLineDTOs = (List<OrderLineDTO>) servletContext.getAttribute("orderLinesDTO");

        model.addAttribute("product", new ItemDTO());
        model.addAttribute("orderLines", Collections.emptyList());
        List<OrderLineDTO> updatedList = ordersService.updateOrderLine(orderLineDTOs, number, itemDTO.getOrderQuantity());

        servletContext.removeAttribute("orderLinesDTO");
        servletContext.setAttribute("orderLinesDTO", updatedList);
        servletContext.removeAttribute("orderDTO");
        servletContext.setAttribute("orderDTO", ordersService.getUpdatedOrderDTO(orderDTO, updatedList));

        return "sales/orderForm";
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
        List<OrderLineDTO> orderLineDTOS = (List<OrderLineDTO>) servletContext.getAttribute("orderLinesDTO");

        ordersService.saveNewOrder(orderDTO, orderLineDTOS);

        orderDTO.setStatus(OrderStatus.INVOICED);

        servletContext.removeAttribute("orderDTO");
        servletContext.setAttribute("orderDTO", orderDTO);

        servletContext.removeAttribute("status");
        servletContext.setAttribute("status", OrderStatus.INVOICED.toString());

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

        if(invoiceDTO != null){
            PDFExporter exporter = new PDFExporter(invoiceDTO);
            exporter.export(response);
        }
    }
}
