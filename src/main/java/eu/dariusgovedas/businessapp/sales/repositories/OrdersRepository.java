package eu.dariusgovedas.businessapp.sales.repositories;

import eu.dariusgovedas.businessapp.sales.entities.Order;
import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    Order findByIdAndOrderType(Long orderNumber, OrderType orderType);

    List<Order> findByStatus(OrderStatus status);
}
