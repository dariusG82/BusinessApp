package eu.dariusgovedas.businessapp.services;

import eu.dariusgovedas.businessapp.accounting.entities.Bank;
import eu.dariusgovedas.businessapp.accounting.entities.BankAccount;
import eu.dariusgovedas.businessapp.accounting.entities.Payment;
import eu.dariusgovedas.businessapp.accounting.entities.dto.BankAccountDTO;
import eu.dariusgovedas.businessapp.accounting.entities.dto.BankDTO;
import eu.dariusgovedas.businessapp.accounting.repositories.BankAccountRepository;
import eu.dariusgovedas.businessapp.accounting.repositories.BankRepository;
import eu.dariusgovedas.businessapp.accounting.repositories.PaymentRepository;
import eu.dariusgovedas.businessapp.accounting.services.AccountingService;
import eu.dariusgovedas.businessapp.accounting.services.BankAccountService;
import eu.dariusgovedas.businessapp.accounting.services.BankService;
import eu.dariusgovedas.businessapp.accounting.services.PaymentService;
import eu.dariusgovedas.businessapp.common.exceptions.BankAccountNotFoundException;
import eu.dariusgovedas.businessapp.common.exceptions.BankNotFoundException;
import eu.dariusgovedas.businessapp.sales.entities.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class AccountingServicesTest {

    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private BankService bankService;
    @Autowired
    private BankAccountService bankAccountService;
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Bank bank;
    @Autowired
    private BankDTO bankDTO;
    @Autowired
    private BankAccount bankAccount;
    @Autowired
    private BankAccountDTO bankAccountDTO;
    @Autowired
    private Order order;
    @Autowired
    private Payment payment;

    @Autowired
    JdbcTemplate jdbc;

    @Value("${sql.script.insert.bank}")
    private String sqlInsertBank;
    @Value("${sql.script.insert.bankAccount}")
    private String sqlInsertBankAccount;
    @Value("${sql.script.insert.payment}")
    private String sqlInsertPayment;
    @Value("${sql.script.delete.bank}")
    private String sqlDeleteBank;
    @Value("${sql.script.delete.bankAccount}")
    private String sqlDeleteBankAccount;
    @Value("${sql.script.delete.payment}")
    private String sqlDeletePayment;

    @BeforeEach
    public void setupBeforeEach() {
        jdbc.execute(sqlInsertBank);
        jdbc.execute(sqlInsertBankAccount);
        jdbc.execute(sqlInsertPayment);

        bankAccountDTO.setAccountNumber("LT122233336666558");
        bankAccountDTO.setBalance(BigDecimal.valueOf(20000));
        bankAccountDTO.setBankName(bank.getName());

        order.setId(25L);
        order.setOrderDate(LocalDate.of(2022,10,25));
        order.setClientName("Verslo kasta");
        order.setSupplierName("Kita klase");
        order.setAmountWithVAT(BigDecimal.valueOf(2563.2f));

        payment.setOrderNumber(order.getId());
        payment.setCustomerName(order.getClientName());
        payment.setSupplierName(order.getSupplierName());
        payment.setDateOfOrder(order.getOrderDate());
        payment.setDateOfPayment(LocalDate.now());
        payment.setPaymentAmount(order.getAmountWithVAT());
    }

    @AfterEach
    public void cleanDatabasesAfterEach() {
        jdbc.execute(sqlDeletePayment);
        jdbc.execute(sqlDeleteBankAccount);
        jdbc.execute(sqlDeleteBank);
    }

    // Bank Service Tests
    @Test
    public void addBank_Test() {
        assertEquals(1, bankRepository.count());

        bankDTO.setBankName("DnB Nord");
        bankDTO.setBankAddress("Kaunas, Streiko g. 35");
        bankDTO.setBankSwift("DNBN20025");

        bankService.addBank(bankDTO);

        Optional<Bank> bankOptional = bankRepository.findById("DNBN20025");

        assertTrue(bankOptional.isPresent());

        Bank newBank = bankOptional.get();

        assertEquals(2, bankRepository.count());
        assertEquals("DnB Nord", newBank.getName());
        assertEquals("Kaunas, Streiko g. 35", newBank.getAddress());
        assertEquals("DNBN20025", newBank.getSwift());
    }

    @Test
    public void addNullBank_Test() {
        assertThrows(BankNotFoundException.class, () -> bankService.addBank(null));
    }

    @Test
    public void addBankAccount_Test() {

        assertEquals(1, bankAccountRepository.count());

        bankAccountDTO.setAccountId(1234L);
        bankAccountDTO.setBankName("Swedbank");
        bankAccountDTO.setAccountNumber("LT1234567890123456");

        bankService.addAccount(bankAccountDTO);

        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findById(1234L);

        assertTrue(bankAccountOptional.isPresent());

        BankAccount newAccount = bankAccountOptional.get();

        assertEquals(2, bankAccountRepository.count());
        assertEquals("Swedbank", newAccount.getBank().getName());
        assertEquals("LT1234567890123456", newAccount.getNumber());

    }

    @Test
    public void addNullBankAccount_Test() {
        assertThrows(BankAccountNotFoundException.class, () -> bankService.addAccount(null));
    }

    @Test
    public void getAllBanks_Test(){
        bankDTO.setBankSwift("SWBLT209");
        bankDTO.setBankName("Swedbank");
        bankDTO.setBankAddress("Vilnius, Kauno g. 35");

        List<BankDTO> expectedList = List.of(bankDTO);

        assertIterableEquals(expectedList, bankService.getAllBanks());

        bankDTO.setBankSwift("SWBLT112");
        bankDTO.setBankName("Swedbank");
        bankDTO.setBankAddress("Vilnius, Kauno g. 35");

        List<BankDTO> notExpectedList = List.of(bankDTO);

        assertNotEquals(notExpectedList, bankService.getAllBanks());
    }

    // Bank Account Service Tests
    @Test
    public void getAllAccount_Test(){
        bank.setName("Swedbank");
        bank.setAddress("Vilnius, Kauno g. 35");
        bank.setSwift("SWBLT209");

        bankAccount.setId(125L);
        bankAccount.setNumber("LT122233336666558");
        bankAccount.setBalance(BigDecimal.valueOf(20000));
        bankAccount.setBank(bank);

        List<BankAccount> expectedList = List.of(bankAccount);

        assertEquals(expectedList.size(), bankAccountService.getAllAccounts().size());
        assertEquals(expectedList.get(0).getId(), bankAccountService.getAllAccounts().get(0).getId());
        assertEquals(expectedList.get(0).getNumber(), bankAccountService.getAllAccounts().get(0).getNumber());
        assertEquals(
                expectedList.get(0).getBalance().stripTrailingZeros(),
                bankAccountService.getAllAccounts().get(0).getBalance().stripTrailingZeros()
        );

    }

    // Payment Service Tests
    @Test
    public void createPayment_Test(){
        assertEquals(payment, paymentService.createPayment(order));
    }

    @Test
    public void savePayment_Test(){
        assertEquals(1, paymentRepository.count());

        payment.setID(UUID.randomUUID());
        paymentService.savePayment(payment);

        assertEquals(2, paymentRepository.count());
    }

    // Accounting Service Tests
}
