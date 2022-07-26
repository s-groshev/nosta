package com.example.server.schemas;

import com.example.server.entity.Shop;
import lombok.Data;

@Data
public class ShopUnitStatisticUnit {
    private String id;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private String name;
    private String parentId;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private ShopUnitType type;  // "CATEGORY"
    private long price;
    private String date;  //2022-05-28T21:12:01.000Z;

    public ShopUnitStatisticUnit(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.parentId = shop.getParent();
        if(shop.getType().equals(ShopUnitType.CATEGORY.toString()))
            this.type = ShopUnitType.CATEGORY;
        else
            this.type = ShopUnitType.OFFER;
        this.price = shop.getPrice();
        this.date = shop.getDateF();
    }
}
