package eu.dariusgovedas.businessapp.sales.entities;

import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private CompanyDTO customer;
    private CompanyDTO supplier;
    private OrderDTO order;
    private List<OrderLineDTO> orderLines;

}
