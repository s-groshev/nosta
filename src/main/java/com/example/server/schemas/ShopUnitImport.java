package com.example.server.schemas;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ShopUnitImport {
    private String id;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private String name;
    private String parentId;  // 3fa85f64-5717-4562-b3fc-2c963f66a333
    private ShopUnitType type;  // "CATEGORY"
    private long price;
    private List<ShopUnit> children;

    @Override
    public String toString() {
        return "ShopUnitImport{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", parentId='" + parentId + '\'' +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}
