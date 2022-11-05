package eu.dariusgovedas.businessapp.accounting.services;

import eu.dariusgovedas.businessapp.accounting.entities.Payment;
import eu.dariusgovedas.businessapp.accounting.repositories.PaymentRepository;
import eu.dariusgovedas.businessapp.sales.entities.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentService {

    private PaymentRepository paymentRepository;

    public Payment createPayment(Order order){
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

    public void savePayment(Payment payment){
        paymentRepository.save(payment);
    }
}
