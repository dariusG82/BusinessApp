package eu.dariusgovedas.businessapp.accounting.services;

import eu.dariusgovedas.businessapp.accounting.entities.Bank;
import eu.dariusgovedas.businessapp.accounting.entities.dto.BankAccountDTO;
import eu.dariusgovedas.businessapp.accounting.entities.dto.BankDTO;
import eu.dariusgovedas.businessapp.accounting.repositories.BankRepository;
import eu.dariusgovedas.businessapp.common.exceptions.BankAccountNotFoundException;
import eu.dariusgovedas.businessapp.common.exceptions.BankNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Getter
@AllArgsConstructor
public class BankService {

    private BankRepository bankRepository;
    private BankAccountService accountService;

    @Transactional
    public void addBank(BankDTO bankDTO) {
        Bank bank = new Bank();

        if (bankDTO == null ||
                bankDTO.getBankSwift() == null ||
                bankDTO.getBankName() == null ||
                bankDTO.getBankAddress() == null){
            throw new BankNotFoundException();
        }

        bank.setSwift(bankDTO.getBankSwift());
        bank.setName(bankDTO.getBankName());
        bank.setAddress(bankDTO.getBankAddress());
        bank.setAccounts(new ArrayList<>());

        bankRepository.save(bank);
    }

    public void addAccount(BankAccountDTO bankAccountDTO) {
        if (bankAccountDTO == null ||
                bankAccountDTO.getBankName() == null ||
                bankAccountDTO.getAccountNumber() == null){
            throw new BankAccountNotFoundException();
        }

        Bank bank = bankRepository.findByNameIgnoreCase(bankAccountDTO.getBankName());

        accountService.saveNewAccount(bank, bankAccountDTO);
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
