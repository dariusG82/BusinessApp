package eu.dariusgovedas.businessapp.accounting.services;

import eu.dariusgovedas.businessapp.accounting.entities.Bank;
import eu.dariusgovedas.businessapp.accounting.entities.BankAccount;
import eu.dariusgovedas.businessapp.accounting.entities.dto.AccountDTO;
import eu.dariusgovedas.businessapp.accounting.entities.dto.BankDTO;
import eu.dariusgovedas.businessapp.accounting.repositories.BankAccountRepository;
import eu.dariusgovedas.businessapp.accounting.repositories.BankRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    public void addAccount(AccountDTO accountDTO) {
        Bank bank = bankRepository.findByNameIgnoreCase(accountDTO.getBankName());

        BankAccount account = new BankAccount();

        account.setId(accountRepository.count() + 1);
        account.setBank(bank);
        account.setNumber(accountDTO.getAccountNumber());
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);
    }

    public List<BankDTO> getAllBanks() {
        List<Bank> banks = bankRepository.findAll();

        List<BankDTO> bankDTOS = new ArrayList<>();

        banks.forEach(bank -> bankDTOS.add(getBankDTO(bank)));

        return bankDTOS;
    }

    private BankDTO getBankDTO(Bank bank) {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setBankName(bank.getName());
        bankDTO.setBankSwift(bank.getSwift());
        bankDTO.setBankAddress(bank.getAddress());

        return bankDTO;
    }
}
