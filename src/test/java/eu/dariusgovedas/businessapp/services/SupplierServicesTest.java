package eu.dariusgovedas.businessapp.services;

import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierItem;
import eu.dariusgovedas.businessapp.common.supplier.entities.SupplierWarehouse;
import eu.dariusgovedas.businessapp.common.supplier.repositories.SupplierItemsRepository;
import eu.dariusgovedas.businessapp.common.supplier.repositories.SupplierWarehouseRepository;
import eu.dariusgovedas.businessapp.common.supplier.services.SupplierItemsService;
import eu.dariusgovedas.businessapp.common.supplier.services.SupplierWarehouseService;
import eu.dariusgovedas.businessapp.companies.entities.Company;
import eu.dariusgovedas.businessapp.companies.enums.CompanyType;
import eu.dariusgovedas.businessapp.companies.repository.CompanyRepository;
import eu.dariusgovedas.businessapp.companies.service.CompanyService;
import eu.dariusgovedas.businessapp.items.entities.ItemCategory;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemDTO;
import eu.dariusgovedas.businessapp.items.entities.dto.ItemPropertiesDTO;
import eu.dariusgovedas.businessapp.items.repository.ItemRepository;
import eu.dariusgovedas.businessapp.items.service.ItemPropertiesService;
import eu.dariusgovedas.businessapp.sales.entities.OrderLineDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class SupplierServicesTest {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private SupplierItemsService supplierItemsService;
    @Autowired
    private SupplierWarehouseService supplierWarehouseService;
    @Autowired
    private ItemPropertiesService itemPropertiesService;

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private SupplierItemsRepository supplierItemsRepository;
    @Autowired
    private SupplierWarehouseRepository warehouseRepository;

    @Autowired
    private Company company;
    @Autowired
    private ItemPropertiesDTO itemPropertiesDTO;
    @Autowired
    private ItemDTO itemDTO;
    @Autowired
    private ItemCategory category;
    @Autowired
    private OrderLineDTO orderLineDTO;
    @Autowired
    private SupplierWarehouse supplierWarehouse;

    @Autowired
    JdbcTemplate jdbc;

    @Value("${sql.script.delete.companies}")
    private String sqlDeleteCompaniesData;

    @Value("${sql.script.delete.supplier}")
    private String sqlDeleteSupplierData;

    @Value("${sql.script.delete.supplierItems}")
    private String sqlDeleteSupplierItems;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    public void setupBeforeEach(){
        company.setCompanyID(123456789L);
        company.setCompanyName("UBA 'Kronu baze'");
        company.setCompanyType(CompanyType.SUPPLIER);
        companyRepository.save(company);

        supplierWarehouse.setWarehouseID(12345L);
        supplierWarehouse.setSupplierName(company.getCompanyName());
        supplierWarehouse.setSupplierItemList(new ArrayList<>());
        warehouseRepository.save(supplierWarehouse);

        itemPropertiesDTO.setCategoryName("Books");

        itemPropertiesService.saveNewCategory(itemPropertiesDTO);

        itemDTO.setItemID(UUID.randomUUID());
        itemDTO.setItemNumber(123456L);
        itemDTO.setName("Piestukas");
        itemDTO.setCategory("Books");
        itemDTO.setDescription("Instrument for books");
        itemDTO.setPurchasePrice(BigDecimal.valueOf(25.36f));
        itemDTO.setSalePrice(BigDecimal.valueOf(45.22f));
        itemDTO.setStockQuantity(25L);
        itemDTO.setOrderQuantity(33L);
        itemDTO.setOrderPrice(BigDecimal.valueOf(25.36f));
    }

    @AfterEach
    public void cleanAfterEach(){

        jdbc.execute(sqlDeleteSupplierItems);
        jdbc.execute(sqlDeleteSupplierData);
        jdbc.execute(sqlDeleteCompaniesData);
    }

    @Test
    public void addItem_Test(){
        List<SupplierItem> supplierItems = supplierItemsRepository.findAll();

        assertEquals(0, supplierItems.size());

        supplierItemsService.addItem(company.getCompanyID(), itemDTO);

        supplierItems = supplierItemsRepository.findAll();

        assertEquals(1, supplierItems.size());

        SupplierItem item = supplierItems.get(0);

        assertEquals(itemDTO.getItemID(), item.getItemID());
    }

    @Test
    public void addItemWithoutID_Test(){
        itemDTO.setItemID(null);

        assertNull(itemDTO.getItemID());

        supplierItemsService.addItem(company.getCompanyID(), itemDTO);

        List<SupplierItem> supplierItems = supplierItemsRepository.findAll();

        assertEquals(1, supplierItems.size());

        SupplierItem item = supplierItemsRepository.findAll().get(0);

        assertNotNull(item);
        assertNotNull(item.getItemID());
    }

}
