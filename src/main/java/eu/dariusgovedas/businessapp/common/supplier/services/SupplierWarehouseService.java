package eu.dariusgovedas.businessapp.common.supplier.services;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierWarehouse;
import eu.dariusgovedas.businessapp.common.supplier.repositories.SupplierWarehouseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SupplierWarehouseService {

    private SupplierWarehouseRepository warehouseRepository;


    public SupplierWarehouse getSupplierWarehouse(String companyName) {
        return warehouseRepository.findBySupplierName(companyName);
    }

    public Long getNewWarehouseNumber(){
        return warehouseRepository.count() + 1;
    }

    public void saveNewWarehouse(SupplierWarehouse warehouse){
        warehouseRepository.save(warehouse);
    }
}
