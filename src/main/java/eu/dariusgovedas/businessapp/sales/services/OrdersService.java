package eu.dariusgovedas.businessapp.sales.services;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierItem;
import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierItemDto;
import eu.dariusgovedas.businessapp.common.supplier.services.SupplierService;
import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import eu.dariusgovedas.businessapp.sales.entities.*;
import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import eu.dariusgovedas.businessapp.sales.repositories.OrderLineRepository;
import eu.dariusgovedas.businessapp.sales.repositories.OrdersRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@AllArgsConstructor
public class OrdersService {
    private static final BigDecimal VAT = BigDecimal.valueOf(0.21);

    private OrdersRepository ordersRepository;
    private OrderLineRepository orderLineRepository;

    private CompanyService companyService;

    private SupplierService supplierService;

    public OrderDTO createNewOrder(Long id, OrderType orderType) {

        Order order = createOrder(id, orderType);

        return getOrderDTOFromOrder(order);
    }

    private OrderDTO getOrderDTOFromOrder(Order order) {
        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setOrderNumber(order.getId());
        orderDTO.setOrderType(order.getOrderType());
        orderDTO.setOrderDate(order.getOrderDate());
        orderDTO.setClient(order.getClientName());
        orderDTO.setSupplier(order.getSupplierName());
        orderDTO.setAmount(order.getOrderAmount());
        orderDTO.setVatAmount(order.getVatAmount());
        orderDTO.setAmountWithVAT(order.getAmountWithVAT());
        orderDTO.setStatus(order.getStatus());

        return orderDTO;
    }

    Order createOrder(Long id, OrderType type){
        Order order = new Order();
        Company company = companyService.getCompanyById(id);
        Company userBusiness = companyService.getBusinessOwner();
        Long orderNr = ordersRepository.count() + 1;

        order.setId(orderNr);
        order.setOrderType(type);
        order.setOrderDate(LocalDate.now());
        if(type.equals(OrderType.PURCHASE)){
            order.setClientName(userBusiness.getCompanyName());
            order.setSupplierName(company.getCompanyName());
        } else {
            order.setClientName(company.getCompanyName());
            order.setSupplierName(userBusiness.getCompanyName());
        }
        order.setStatus(OrderStatus.OPEN);
        order.setOrderAmount(BigDecimal.ZERO);

        return order;
    }

    public List<OrderLineDTO> updateOrderLine(List<OrderLineDTO> orderLineDTOS, Long itemNumber, Long quantity) {

        for(OrderLineDTO orderLineDTO : orderLineDTOS){
            if(orderLineDTO.getItemNumber().equals(itemNumber)){
                if(quantity == null || quantity <= 0){
                    quantity = 0L;
                }
                orderLineDTO.setOrderQuantity(quantity);
                orderLineDTO.setLinePrice(BigDecimal.valueOf(quantity).multiply(orderLineDTO.getPurchasePrice()));
            }
        }

        return orderLineDTOS;
    }

    public List<OrderLineDTO> getOrderLines(OrderDTO order) {
        List<OrderLine> orderLines = orderLineRepository.findOrderLines(order.getOrderNumber());
        List<OrderLineDTO> orderLineDTOS = new ArrayList<>();

        if(orderLines.size() == 0){
            return null;
        }

        for(OrderLine orderLine : orderLines){
            if(orderLine.getQuantity() != null && orderLine.getQuantity() > 0){
                OrderLineDTO orderLineDTO = getOrderLineDTOFromOrderLine(orderLine);
                orderLineDTOS.add(orderLineDTO);
            }
        }

        return orderLineDTOS;
    }

    private OrderLineDTO getOrderLineDTOFromOrderLine(OrderLine orderLine) {
        OrderLineDTO orderLineDTO = new OrderLineDTO();
        orderLineDTO.setItemName(orderLine.getItemName());
        orderLineDTO.setOrderQuantity(orderLine.getQuantity());
        orderLineDTO.setPurchasePrice(orderLine.getPurchasePrice());
        orderLineDTO.setLinePrice(orderLine.getTotalPrice());
        return orderLineDTO;
    }

    private OrderLine getOrderLineFromDTO(OrderLineDTO orderLineDTO) {
        OrderLine orderLine = new OrderLine();

        orderLine.setId(orderLineRepository.count() + 1);
        orderLine.setItemName(orderLineDTO.getItemName());
        orderLine.setQuantity(orderLineDTO.getOrderQuantity());
        orderLine.setPurchasePrice(orderLineDTO.getPurchasePrice());
        orderLine.setTotalPrice(orderLineDTO.getLinePrice());

        return orderLine;
    }

    public OrderDTO getUpdatedOrderDTO(OrderDTO orderDTO, List<OrderLineDTO> orderLineDTOS){
        BigDecimal orderAmount = BigDecimal.ZERO;
        for(OrderLineDTO lineDTO : orderLineDTOS){
            orderAmount = orderAmount.add(lineDTO.getLinePrice());
        }

        BigDecimal vatAmount = orderAmount.multiply(VAT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountWithVAT = orderAmount.add(vatAmount);

        orderDTO.setAmount(orderAmount);
        orderDTO.setVatAmount(vatAmount);
        orderDTO.setAmountWithVAT(amountWithVAT);

        return orderDTO;
    }

    public InvoiceDTO getInvoiceDTO(OrderDTO orderDTO) {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        CompanyDTO companyDTO = companyService.getClientDTOByName(orderDTO.getClient());
        CompanyDTO supplierDTO = companyService.getClientDTOByName(orderDTO.getSupplier());

        List<OrderLine> orderLines = orderLineRepository.findOrderLines(orderDTO.getOrderNumber());
        List<OrderLineDTO> orderLineDTOS = new ArrayList<>();
        if(!(orderLines == null)){
            for(OrderLine line : orderLines){
                orderLineDTOS.add(getOrderLineDTOFromOrderLine(line));
            }
        }

        invoiceDTO.setCustomer(companyDTO);
        invoiceDTO.setSupplier(supplierDTO);
        invoiceDTO.setOrder(orderDTO);
        invoiceDTO.setOrderLines(orderLineDTOS);

        return invoiceDTO;
    }

    public Page<OrderDTO> getOpenOrdersDTOs(Pageable pageable) {
        List<Order> orders = ordersRepository.findByStatus(OrderStatus.INVOICED);
        List<OrderDTO> orderDTOS = getOrderDTOS(orders);

        return new PageImpl<>(orderDTOS, pageable, orderDTOS.size());
    }

    private List<OrderDTO> getOrderDTOS(List<Order> orders) {
        List<OrderDTO> orderDTOS = new ArrayList<>();

        if(orders != null){
            for (Order order : orders){
                orderDTOS.add(getOrderDTOFromOrder(order));
            }
        }

        return orderDTOS;
    }

    public Order getOrderByID(Long orderID) {
        return ordersRepository.findById(orderID).orElseThrow(IllegalArgumentException::new);
    }

    public Page<SupplierItemDto> getSupplierStock(Long supplierID, Pageable pageable) {
        Page<SupplierItem> supplierStock = supplierService.getSupplierStock(supplierID, pageable);
        List<SupplierItemDto> itemDTOS = new ArrayList<>();

        for(SupplierItem supplierItem : supplierStock){
            SupplierItemDto supplierItemDto = new SupplierItemDto();
            supplierItemDto.setItemID(supplierItem.getItemID());
            supplierItemDto.setItemName(supplierItem.getItemName());
            supplierItemDto.setItemDescription(supplierItem.getItemDescription());
            supplierItemDto.setPrice(supplierItem.getPrice());
            supplierItemDto.setQuantity(supplierItem.getQuantity());
            itemDTOS.add(supplierItemDto);
        }

        return new PageImpl<>(itemDTOS, pageable, itemDTOS.size());
    }

    public List<OrderLineDTO> getSupplierOrderLines(Long supplierID, Long orderNumber) {
        List<SupplierItem> supplierItems = supplierService.getSupplierStock(supplierID, Pageable.unpaged()).stream().toList();
        List<OrderLineDTO> orderLineDTOS = new ArrayList<>();

        for (SupplierItem item : supplierItems){
            orderLineDTOS.add(createOrderLineDTOForSupplierItem(item, orderNumber));
        }

        return orderLineDTOS;
    }

    private OrderLineDTO createOrderLineDTOForSupplierItem(SupplierItem item, Long orderNumber) {
        OrderLineDTO orderLineDTO = new OrderLineDTO();

        orderLineDTO.setItemNumber(item.getItemNumber());
        orderLineDTO.setItemName(item.getItemName());
        orderLineDTO.setStockQuantity(item.getQuantity());
        orderLineDTO.setOrderQuantity(0L);
        orderLineDTO.setPurchasePrice(item.getPrice());
        orderLineDTO.setLinePrice(BigDecimal.ZERO);
        orderLineDTO.setOrderID(orderNumber);

        return orderLineDTO;
    }

    @Transactional
    public void saveNewOrder(OrderDTO orderDTO, List<OrderLineDTO> orderLineDTOS) {
        Order order = new Order();

        order.setId(orderDTO.getOrderNumber());
        order.setOrderType(orderDTO.getOrderType());
        order.setOrderDate(orderDTO.getOrderDate());
        order.setClientName(orderDTO.getClient());
        order.setSupplierName(orderDTO.getSupplier());
        order.setOrderAmount(orderDTO.getAmount());
        order.setVatAmount(orderDTO.getVatAmount());
        order.setAmountWithVAT(orderDTO.getAmountWithVAT());
        order.setStatus(OrderStatus.INVOICED);
        order.setOrderLines(new ArrayList<>());

        ordersRepository.save(order);

        long lineNr = 1L;

        for (OrderLineDTO lineDTO : orderLineDTOS){
            if(lineDTO.getOrderQuantity() != null && lineDTO.getOrderQuantity() > 0){
                OrderLine orderLine = getOrderLineFromDTO(lineDTO);
                orderLine.setLineNumber(lineNr++);
                order.addOrderLine(orderLine);
                orderLineRepository.save(orderLine);
            }
        }

    }
}
