package com.example.server.schemas;
import com.example.server.entity.Shop;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopUnit {
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ShopUnit> children;
    private String date;  //2022-05-28T21:12:01.000Z;
    private String id;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private String name;
    private String parentId;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private long price;
    private ShopUnitType type;  // "CATEGORY"


    public ShopUnit(Shop shop) {
        this.id = shop.getId();
        this.name = shop.getName();
        this.date = shop.getDateF();
        this.parentId = shop.getParent();
        if (shop.getType().equals("CATEGORY")) {
            this.type = ShopUnitType.CATEGORY;
            this.children = new ArrayList<>();
        } else {
            this.type = ShopUnitType.OFFER;
            this.children = null;
        }
        this.price = shop.getPrice();
    }
}
