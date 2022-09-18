package eu.dariusgovedas.businessapp.sales.repositories;

import eu.dariusgovedas.businessapp.sales.entities.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

    @Query("FROM OrderLine line WHERE line.order.id = :order")
    List<OrderLine> findOrderLines(@Param("order") Long orderNumber);
}
