package eu.dariusgovedas.businessapp.accounting.entities;

import eu.dariusgovedas.businessapp.accounting.enums.CashRecordType;
import eu.dariusgovedas.businessapp.users.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class CashRecord {

    @Id
    private UUID id;

    private LocalDate date;

    private BigDecimal cashAmount;

    @Enumerated(value = EnumType.STRING)
    private CashRecordType recordType;

    @ManyToOne
    private User user;
}
