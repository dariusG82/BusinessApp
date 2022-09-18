package eu.dariusgovedas.businessapp.sales.services;

import eu.dariusgovedas.businessapp.clients.entities.Client;
import eu.dariusgovedas.businessapp.clients.service.ClientService;
import eu.dariusgovedas.businessapp.items.entities.ItemDTO;
import eu.dariusgovedas.businessapp.items.service.ItemService;
import eu.dariusgovedas.businessapp.sales.entities.Order;
import eu.dariusgovedas.businessapp.sales.entities.OrderDTO;
import eu.dariusgovedas.businessapp.sales.entities.OrderLine;
import eu.dariusgovedas.businessapp.sales.entities.OrderLineDTO;
import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import eu.dariusgovedas.businessapp.sales.repositories.OrderLineRepository;
import eu.dariusgovedas.businessapp.sales.repositories.OrdersRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@AllArgsConstructor
public class OrdersService {

    private OrdersRepository ordersRepository;
    private OrderLineRepository orderLineRepository;

    private ClientService clientService;
    private ItemService itemService;

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

    @Transactional
    Order createOrder(Long id, OrderType type){
        Order order = new Order();
        Client client = clientService.getClientById(id);
        Client userBusiness = clientService.getBusinessOwner();
        Long orderNr = ordersRepository.count() + 1;

        order.setId(orderNr);
        order.setOrderType(type);
        order.setOrderDate(LocalDate.now());
        if(type.equals(OrderType.PURCHASE)){
            order.setClientName(userBusiness.getBusinessName());
            order.setSupplierName(client.getBusinessName());
        } else {
            order.setClientName(client.getBusinessName());
            order.setSupplierName(userBusiness.getBusinessName());
        }
        order.setStatus(OrderStatus.OPEN);
        order.setOrderAmount(BigDecimal.ZERO);

        ordersRepository.save(order);

        return order;
    }

    public OrderLineDTO updateOrderLine(Long orderNumber, Long itemNumber, OrderLineDTO orderLineDTO) {
        OrderLineDTO lineDTO = getUpdatedOrderLineDTO(itemNumber);
        lineDTO.setOrderID(orderNumber);
        lineDTO.setOrderQuantity(orderLineDTO.getOrderQuantity());
        lineDTO.setLinePrice(BigDecimal.valueOf(orderLineDTO.getOrderQuantity()).multiply(lineDTO.getPurchasePrice()));

        return lineDTO;
    }

    public OrderLineDTO getNewOrderLine(Long itemNumber, OrderDTO orderDTO) {
        OrderLineDTO orderLineDTO = getUpdatedOrderLineDTO(itemNumber);
        orderLineDTO.setOrderID(orderDTO.getOrderNumber());

        return orderLineDTO;
    }

    private OrderLineDTO getUpdatedOrderLineDTO(Long itemNumber) {
        OrderLineDTO orderLineDTO = new OrderLineDTO();
        ItemDTO itemDTO = itemService.getItemDTOByItemNumber(itemNumber);
        orderLineDTO.setItemNumber(itemDTO.getItemNumber());
        orderLineDTO.setItemName(itemDTO.getName());
        orderLineDTO.setStockQuantity(itemDTO.getStockQuantity());
        orderLineDTO.setPurchasePrice(itemDTO.getSalePrice());

        return orderLineDTO;
    }

    @Transactional
    public void saveNewOrderLine(OrderDTO orderDTO, Long number, Long quantity) {
        Order order = ordersRepository.findByIdAndOrderType(orderDTO.getOrderNumber(), orderDTO.getOrderType());
        Long lineNumber = (long) order.getOrderLines().size() + 1;
        OrderLine orderLine = new OrderLine();
        Long id = orderLineRepository.count() + 1;
        orderLine.setId(id);
        orderLine.setLineNumber(lineNumber);
        ItemDTO item = itemService.getItemDTOByItemNumber(number);
        orderLine.setItemName(item.getName());
        orderLine.setQuantity(quantity);
        orderLine.setPurchasePrice(item.getSalePrice());
        orderLine.setOrder(order);
        orderLine.setTotalPrice(orderLine.getTotalPrice());

        order.addOrderLine(orderLine);
        updateOrderAmount(order);
    }

    private void updateOrderAmount(Order order) {
        order.setOrderAmount(order.getOrderAmount());
        order.setVatAmount(order.getVatAmount());
        order.setAmountWithVAT(order.getAmountWithVAT());
    }

    public List<OrderLineDTO> getOrderLines(OrderDTO order) {
        List<OrderLine> orderLines = orderLineRepository.findOrderLines(order.getOrderNumber());
        List<OrderLineDTO> orderLineDTOS = new ArrayList<>();

        if(orderLines.size() == 0){
            return null;
        }

        for(OrderLine orderLine : orderLines){
            OrderLineDTO orderLineDTO = getOrderLineDTOFromOrderLine(orderLine);
            orderLineDTOS.add(orderLineDTO);
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

    public OrderDTO getUpdatedOrderDTO(OrderDTO order) {
        Order updatedOrder = ordersRepository.findByIdAndOrderType(order.getOrderNumber(), order.getOrderType());

        return getOrderDTOFromOrder(updatedOrder);
    }
}
