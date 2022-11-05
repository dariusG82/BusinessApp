package eu.dariusgovedas.businessapp.accounting.services;

import eu.dariusgovedas.businessapp.accounting.entities.Bank;
import eu.dariusgovedas.businessapp.accounting.entities.BankAccount;
import eu.dariusgovedas.businessapp.accounting.entities.dto.BankAccountDTO;
import eu.dariusgovedas.businessapp.accounting.repositories.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    public List<BankAccount> getAllAccounts() {
        return bankAccountRepository.findAll();
    }

    @Transactional
    public void saveNewAccount(Bank bank, BankAccountDTO bankAccountDTO) {

        BankAccount account = new BankAccount();
        account.setId(getNewAccountID());
        account.setBank(bank);
        account.setNumber(bankAccountDTO.getAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        bankAccountRepository.save(account);
    }

    private Long getNewAccountID(){
        return bankAccountRepository.count() + 1;
    }
}
