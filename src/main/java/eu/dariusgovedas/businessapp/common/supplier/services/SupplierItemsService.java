package eu.dariusgovedas.businessapp.common.supplier.services;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierItem;
import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierWarehouse;
import eu.dariusgovedas.businessapp.common.supplier.repositories.SupplierItemsRepository;
import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.entities.dto.CompanyDTO;
import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.service.ItemPropertiesService;
import eu.dariusgovedas.businessapp.items.service.ItemService;
import eu.dariusgovedas.businessapp.sales.entities.OrderLineDTO;
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
public class SupplierItemsService {

    private final SupplierItemsRepository supplierItemsRepository;

    private final CompanyService companyService;
    private final ItemService itemService;
    private final ItemPropertiesService itemPropertiesService;
    private final SupplierWarehouseService warehouseService;

    public Page<CompanyDTO> getSuppliers(Pageable pageable) {

        return companyService.getSuppliers(pageable);
    }

    public Page<SupplierItem> getSupplierStock(Long supplierID, Pageable pageable) {
        Company company = companyService.getCompanyById(supplierID);

        if(company != null && company.getCompanyType().equals(CompanyType.SUPPLIER)){
            SupplierWarehouse warehouse = warehouseService.getSupplierWarehouse(company.getCompanyName());
            List<SupplierItem> items = warehouse.getSupplierItemList();
            return new PageImpl<>(items, pageable, items.size());
        }

        return new PageImpl<>(new ArrayList<>(), pageable, 0);
    }

    @Transactional
    public void addItem(Long companyID, ItemDTO itemDTO) {
        SupplierItem supplierItem = new SupplierItem();
        Company company = companyService.getCompanyById(companyID);
        SupplierWarehouse supplierWarehouse = warehouseService.getSupplierWarehouse(company.getCompanyName());
        ItemCategory category = itemPropertiesService.getItemCategory(itemDTO.getCategory());

        UUID itemID;
        if(itemDTO.getItemID() == null){
            itemID = UUID.randomUUID();
        } else {
            itemID = itemDTO.getItemID();
        }
        supplierItem.setItemID(itemID);
        itemDTO.setItemID(supplierItem.getItemID());
        supplierItem.setItemNumber(generateItemNumber(category));
        itemDTO.setItemNumber(supplierItem.getItemNumber());
        supplierItem.setWarehouse(supplierWarehouse);
        supplierItem.setItemName(itemDTO.getName());
        supplierItem.setItemDescription(itemDTO.getDescription());
        supplierItem.setCategory(itemDTO.getCategory());
        supplierItem.setPrice(itemDTO.getSalePrice());
        supplierItem.setQuantity(itemDTO.getStockQuantity());

        itemService.createItem(itemDTO);

        supplierItemsRepository.save(supplierItem);

    }

    @Transactional
    public void updateSupplierStock(String supplierName, List<OrderLineDTO> orderLineDTOS) {
        SupplierWarehouse warehouse = warehouseService.getSupplierWarehouse(supplierName);

        for (OrderLineDTO lineDTO : orderLineDTOS){
            UUID itemID = itemService.getItemID(lineDTO.getItemNumber());
            SupplierItem item = supplierItemsRepository.findByWarehouseAndItemId(warehouse.getWarehouseID(), itemID);
            Long quantity = lineDTO.getStockQuantity() - lineDTO.getOrderQuantity();
            item.setQuantity(quantity);
            supplierItemsRepository.save(item);
        }
    }

    private Long generateItemNumber(ItemCategory category) {
        return itemService.generateItemNumber(category);
    }
}
