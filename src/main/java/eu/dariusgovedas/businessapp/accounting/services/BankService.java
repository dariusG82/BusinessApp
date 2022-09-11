package eu.dariusgovedas.businessapp.accounting.services;

import eu.dariusgovedas.businessapp.accounting.entities.Bank;
import eu.dariusgovedas.businessapp.accounting.entities.BankAccount;
import eu.dariusgovedas.businessapp.accounting.entities.BankDTO;
import eu.dariusgovedas.businessapp.accounting.repositories.BankAccountRepository;
import eu.dariusgovedas.businessapp.accounting.repositories.BankRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@Setter
@Getter
@AllArgsConstructor
public class BankService {

    private BankRepository bankRepository;
    private BankAccountRepository accountRepository;

    @Transactional
    public void addBank(BankDTO bankDTO) {
        Bank bank = new Bank();

        bank.setSwift(bankDTO.getBankSwift());
        bank.setName(bankDTO.getBankName());
        bank.setAddress(bankDTO.getBankAddress());
        bank.setAccounts(new ArrayList<>());

        bankRepository.save(bank);
    }

    @Transactional
    public void addAccount(BankDTO bankDTO) {
        Bank bank = bankRepository.findByNameIgnoreCase(bankDTO.getBankName());

        BankAccount account = new BankAccount();

        account.setId(accountRepository.count() + 1);
        account.setBank(bank);
        account.setNumber(bankDTO.getBankAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);
    }
}
