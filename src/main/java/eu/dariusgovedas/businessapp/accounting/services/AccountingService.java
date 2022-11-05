package eu.dariusgovedas.businessapp.accounting.services;

import eu.dariusgovedas.businessapp.accounting.entities.BankAccount;
import eu.dariusgovedas.businessapp.accounting.entities.Payment;
import eu.dariusgovedas.businessapp.sales.entities.Order;
import eu.dariusgovedas.businessapp.sales.entities.OrderDTO;
import eu.dariusgovedas.businessapp.sales.enums.OrderStatus;
import eu.dariusgovedas.businessapp.sales.services.OrdersService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountingService {

    private OrdersService ordersService;
    private PaymentService paymentService;
    private BankAccountService accountService;

    public Page<OrderDTO> getAllOpenOrders(Pageable pageable) {

        return ordersService.getOrdersByStatus(pageable, OrderStatus.INVOICED);
    }

    @Transactional
    public void makePayment(Long orderID) {

        Order order = ordersService.getOrderByID(orderID);

        Payment payment = paymentService.createPayment(order);

        boolean isPaymentSuccess = tryToPayForOrder(payment);

        if(isPaymentSuccess){
            paymentService.savePayment(payment);
            order.setStatus(OrderStatus.CONFIRMED);
        }
    }

    private boolean tryToPayForOrder(Payment payment){

        List<BankAccount> accountList = accountService.getAllAccounts();

        for (BankAccount account : accountList) {
            if (account.getBalance().compareTo(payment.getPaymentAmount()) > 0) {
                account.setBalance(account.getBalance().subtract(payment.getPaymentAmount()));
                return true;
            }
        }

        return false;
    }
}
