package eu.dariusgovedas.businessapp.common.supplier.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Setter
@Getter
public class SupplierWarehouse {

    @Id
    private Long warehouseID;

    private String supplierName;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "warehouse",
            orphanRemoval = true
    )
    private List<SupplierItem> supplierItemList;

    public void addSupplierItem(SupplierItem item){
        supplierItemList.add(item);
        item.setWarehouse(this);
    }

    public void removeSupplierItem(SupplierItem item){
        supplierItemList.remove(item);
        item.setWarehouse(null);
    }
}
