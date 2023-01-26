package eu.dariusgovedas.businessapp.items.entities;

import lombok.Getter;
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
public class ItemCategory {

    @Id
    private Long id;
    private String categoryName;

    @OneToMany(
            cascade = CascadeType.ALL,
            mappedBy = "category",
            orphanRemoval = true
    )
    private List<Item> items;
}
