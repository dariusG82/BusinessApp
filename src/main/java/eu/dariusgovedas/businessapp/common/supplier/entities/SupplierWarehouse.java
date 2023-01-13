package eu.dariusgovedas.businessapp.common.supplier.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Setter
@Getter
@Component
@NoArgsConstructor
@AllArgsConstructor
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
