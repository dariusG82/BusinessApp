package eu.dariusgovedas.businessapp.sales.services;

import eu.dariusgovedas.businessapp.sales.entities.OrderLine;
import eu.dariusgovedas.businessapp.sales.repositories.OrderLineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderLineService {

    private OrderLineRepository orderLineRepository;

    public List<OrderLine> getOrderLinesByOrder(Long orderNumber){
        return orderLineRepository.findOrderLines(orderNumber);
    }

    public Long getNewOrderLineID(){
        return orderLineRepository.count() + 1;
    }

    public void saveOrderLine(OrderLine orderLine){
        orderLineRepository.save(orderLine);
    }
}
