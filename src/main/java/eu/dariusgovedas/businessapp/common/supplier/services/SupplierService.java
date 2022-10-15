package eu.dariusgovedas.businessapp.common.supplier.services;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierItem;
import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierWarehouse;
import eu.dariusgovedas.businessapp.common.supplier.repositories.SupplierItemsRepository;
import eu.dariusgovedas.businessapp.common.supplier.repositories.SupplierWarehouseRepository;
import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.repository.ItemRepository;
import eu.dariusgovedas.businessapp.items.service.ItemPropertiesService;
import eu.dariusgovedas.businessapp.items.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SupplierService {

    private final CompanyService companyService;
    private final ItemService itemService;
    private final SupplierWarehouseRepository supplierWarehouseRepository;
    private final SupplierItemsRepository supplierItemsRepository;
    private final ItemRepository itemRepository;
    private final ItemPropertiesService itemPropertiesService;

    public Page<CompanyDTO> getSuppliers(Pageable pageable) {

        return companyService.getSuppliers(pageable);
    }

    public Page<SupplierItem> getSupplierStock(Long supplierID, Pageable pageable) {
        Company company = companyService.getCompanyById(supplierID);

        if(company != null && company.getCompanyType().equals(CompanyType.SUPPLIER)){
            SupplierWarehouse warehouse = supplierWarehouseRepository.findBySupplierName(company.getCompanyName());
            List<SupplierItem> items = warehouse.getSupplierItemList();
            return new PageImpl<>(items, pageable, items.size());
        }

        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    @Transactional
    public void addItem(Long id, ItemDTO itemDTO) {
        SupplierItem supplierItem = new SupplierItem();
        Company company = companyService.getCompanyById(id);
        SupplierWarehouse supplierWarehouse = supplierWarehouseRepository.findBySupplierName(company.getCompanyName());
        ItemCategory category = itemPropertiesService.getItemCategory(itemDTO.getCategory());

        supplierItem.setItemID(UUID.randomUUID());
        supplierItem.setItemNumber(generateItemNumber(category));
        supplierItem.setWarehouse(supplierWarehouse);
        supplierItem.setItemName(itemDTO.getName());
        supplierItem.setItemDescription(itemDTO.getDescription());
        supplierItem.setCategory(itemDTO.getCategory());
        supplierItem.setPrice(itemDTO.getSalePrice());
        supplierItem.setQuantity(itemDTO.getStockQuantity());

        itemService.createItem(itemDTO);

        supplierItemsRepository.save(supplierItem);

    }

    private Long generateItemNumber(ItemCategory category) {
        long itemNr = itemRepository.findItemByCategory(category).size() + 1L;
        return category.getId() * 100000 + itemNr;
    }

    public SupplierItem getSupplierItemStock(String supplier, UUID itemID) {
        SupplierWarehouse warehouse = supplierWarehouseRepository.findBySupplierName(supplier);
        Long warehouseID = warehouse.getWarehouseID();
        SupplierItem supplierItem = supplierItemsRepository.findByWarehouseAndItemId(warehouseID, itemID);
        return supplierItem != null ?  supplierItem : new SupplierItem();
    }
}