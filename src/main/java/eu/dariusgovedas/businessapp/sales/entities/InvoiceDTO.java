package eu.dariusgovedas.businessapp.sales.entities;

import eu.dariusgovedas.businessapp.clients.entities.ClientDTO;
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

    private ClientDTO customer;
    private ClientDTO supplier;
    private OrderDTO order;
    private List<OrderLineDTO> orderLines;

}
