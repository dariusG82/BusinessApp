package eu.dariusgovedas.businessapp.accounting.services;

import eu.dariusgovedas.businessapp.accounting.entities.BankAccount;
import eu.dariusgovedas.businessapp.accounting.entities.Payment;
import eu.dariusgovedas.businessapp.accounting.repositories.BankAccountRepository;
import eu.dariusgovedas.businessapp.accounting.repositories.PaymentRepository;
import eu.dariusgovedas.businessapp.sales.entities.Order;
import eu.dariusgovedas.businessapp.sales.entities.OrderDTO;
import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.services.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountingService {

    private OrdersService ordersService;
    private PaymentRepository paymentRepository;
    private BankAccountRepository accountRepository;

    public Page<OrderDTO> getAllOpenOrders(Pageable pageable) {

        return ordersService.getOpenOrdersDTOs(pageable);
    }

    @Transactional
    public void makePayment(Long orderID) {

        Order order = ordersService.getOrderByID(orderID);

        Payment payment = createPayment(order);

        paymentRepository.save(payment);

        payForOrder(payment);

        order.setStatus(OrderStatus.CONFIRMED);
    }

    private Payment createPayment(Order order){
        Payment payment = new Payment();
        payment.setID(UUID.randomUUID());
        payment.setOrderNumber(order.getId());
        payment.setCustomerName(order.getClientName());
        payment.setSupplierName(order.getSupplierName());
        payment.setDateOfOrder(order.getOrderDate());
        payment.setDateOfPayment(LocalDate.now());
        payment.setPaymentAmount(order.getAmountWithVAT());

        return payment;
    }

    private void payForOrder(Payment payment){
        BankAccount account = accountRepository.findById(1L).orElseThrow(IllegalArgumentException::new);

        account.setBalance(account.getBalance().subtract(payment.getPaymentAmount()));
    }
}
