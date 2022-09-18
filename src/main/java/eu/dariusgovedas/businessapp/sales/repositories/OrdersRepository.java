package eu.dariusgovedas.businessapp.sales.repositories;

import eu.dariusgovedas.businessapp.sales.entities.Order;
import eu.dariusgovedas.businessapp.sales.enums.OrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    Order findByIdAndOrderType(Long orderNumber, OrderType orderType);
}
