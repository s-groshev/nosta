package com.example.server.repository;

import com.example.server.entity.Shop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends CrudRepository<Shop, String> {
    List<Shop> findAllByParent(String parent);
    List<Shop> findAllByType(String type);
}
