package com.example.server.repository;

import com.example.server.entity.ShopStatistic;
import com.example.server.entity.ShopStatisticId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopStatisticRepository extends CrudRepository<ShopStatistic, ShopStatisticId> {
    List<ShopStatistic> findAllByUuid(String id);
}
