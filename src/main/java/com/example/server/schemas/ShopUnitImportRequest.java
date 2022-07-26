package com.example.server.schemas;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ShopUnitImportRequest {
    private List<ShopUnitImport>  items;
    private String updateDate;  //2022-05-28T21:12:01.000Z;
}
