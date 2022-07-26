package com.example.server.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ShopStatistic")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ShopStatistic {
    @EmbeddedId
    private ShopStatisticId statisticId;
    private String uuid;
    private String dateF;
    private long price;


    public ShopStatistic(Shop shop) {
        this.statisticId = new ShopStatisticId(shop.getId(),shop.getDateF());
        this.uuid = shop.getId();
        this.dateF = shop.getDateF();
        this.price = shop.getPrice();
    }
}
