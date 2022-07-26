package com.example.server.entity;

import com.example.server.schemas.ShopUnit;
import com.example.server.schemas.ShopUnitImport;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Shop")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Shop {
    @Id
    private String id;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private String name;
    private String parent;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private String type;  // "CATEGORY"
    private long price;
    private String dateF;

    public Shop(ShopUnitImport shopUnitImport,String date) {
        this.id = shopUnitImport.getId();
        this.name = shopUnitImport.getName();
        this.parent = shopUnitImport.getParentId();
        this.type = shopUnitImport.getType().toString();
        this.price = shopUnitImport.getPrice();
        this.dateF = date;
    }

    public Shop(ShopUnit shopUnit) {
        this.id = shopUnit.getId();
        this.name = shopUnit.getName();
        this.parent = shopUnit.getParentId();
        this.type = shopUnit.getType().toString();
        this.price = shopUnit.getPrice();
    }
}