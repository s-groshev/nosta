package com.example.server.schemas;

import lombok.*;

import java.util.List;

@Data
public class ShopUnitStatisticResponse {
    List<ShopUnitStatisticUnit> items;
}
