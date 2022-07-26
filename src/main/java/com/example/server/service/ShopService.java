package com.example.server.service;

import com.example.server.entity.Shop;
import com.example.server.entity.ShopStatistic;
import com.example.server.exeption.Error;
import com.example.server.exeption.ErrorException400;
import com.example.server.exeption.ErrorException404;
import com.example.server.repository.ShopRepository;
import com.example.server.repository.ShopStatisticRepository;
import com.example.server.schemas.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ShopService {

    ShopRepository shopRepository;
    ShopStatisticRepository statisticRepository;

    public ResponseEntity<Error> postProducts(ShopUnitImportRequest products) {
        String date=products.getUpdateDate();
        List<ShopUnitImport> shopUnitImport = products.getItems();
        for (ShopUnitImport item:shopUnitImport) {
            Shop shop =new Shop(item,date);
            if(shopRepository.existsById(shop.getId())) {
                Date d = validDateTIme(shop.getDateF());
                Shop old = shopRepository.findById(shop.getId()).get();
                Date d2 = validDateTIme(old.getDateF());
                if (d.compareTo(d2)>=0) {
                    shopRepository.save(shop);
                    statisticRepository.save(new ShopStatistic(shop));
                    updateDate(shop);
                }
                else
                    throw new ErrorException400("Validation Failed");
            } else {
                validDateTIme(shop.getDateF());
                shopRepository.save(shop);
                statisticRepository.save(new ShopStatistic(shop));
                updateDate(shop);
            }
        }
        // обновление стоимостей и дат категорий
        List<Shop> listCategory = shopRepository.findAllByType(ShopUnitType.CATEGORY.toString());
        for (Shop category : listCategory) {
            long summa = countMoney(category);
            int childs = countChild(category);
            if (childs != 0)
                category.setPrice(summa/childs);
            else
                category.setPrice(0);
            shopRepository.save(category);
            statisticRepository.save(new ShopStatistic(category));
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    private Date validDateTIme(String dateTimeISO8601){
        Date date;
        try {
            TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(dateTimeISO8601);
            Instant i = Instant.from(ta);
            date = Date.from(i);
        } catch (DateTimeParseException e) {
            throw new ErrorException400("Validation Failed");
        }
        return date;
    }
    private void updateDate(Shop shop) {
        if (shop.getParent() != null && shopRepository.existsById(shop.getParent())) {
            //сравнить даты
            Date d = validDateTIme(shop.getDateF());
            Shop parent = shopRepository.findById(shop.getParent()).get();
            Date d2 = validDateTIme(parent.getDateF());
            if (d.compareTo(d2)>=0) {
                parent.setDateF(shop.getDateF());
                shopRepository.save(parent);
                statisticRepository.save(new ShopStatistic(shop));
                updateDate(parent);
            }
        }
    }
    private long countMoney(Shop shop){
        long m = 0;
        if (shop.getType().equals(ShopUnitType.OFFER.toString())) {
            m = shop.getPrice();
        } else {
            List<Shop> children = shopRepository.findAllByParent(shop.getId());
            for (Shop child : children) { m += countMoney(child); }
        }
        return m;
    }
    private int countChild(Shop shop){
        int i = 0;
        if (shop.getType().equals(ShopUnitType.OFFER.toString())) {
            i = 1;
        } else {
            List<Shop> children = shopRepository.findAllByParent(shop.getId());
            for (Shop child : children) { i += countChild(child); }
        }
        return i;
    }

    public ResponseEntity<Error> deleteProduct(String id) {
        if(shopRepository.existsById(id)){
            deleteShop(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            throw new ErrorException404("Item not found");
        }
    }
    private void deleteShop(String id) {
        Shop shop = shopRepository.findById(id).get();
        if (shop.getType().equals(ShopUnitType.CATEGORY.toString())) {
            List<Shop> children = shopRepository.findAllByParent(id);
            for (Shop shops : children) {
                deleteShop(shops.getId());
            }
        }
        shopRepository.deleteById(id);
        statisticRepository.deleteAll(statisticRepository.findAllByUuid(id));
    }

    public ShopUnit getProduct(String id) {
        if(shopRepository.existsById(id)){
            return getShop(id);
        } else {
            throw new ErrorException404("Item not found");
        }
    }
    private ShopUnit getShop(String id) {
        Shop shop = shopRepository.findById(id).get();
        ShopUnit shopUnit = new ShopUnit(shop);
        if (shop.getType().equals(ShopUnitType.OFFER.toString())) {
            return shopUnit;
        } else {
            List<Shop> children = shopRepository.findAllByParent(id);
            List<ShopUnit> childrenS = new ArrayList<>();
            for (Shop shopChildren : children) {
                if (shopChildren.getType().equals(ShopUnitType.CATEGORY.toString())) {
                    childrenS.add(getShop(shopChildren.getId()));
                } else {
                    childrenS.add(new ShopUnit(shopChildren));
                }
            }
            shopUnit.setChildren(childrenS);
            return shopUnit;
        }
    }

    public List<Shop> getAllByParent(String id) {
        return shopRepository.findAllByParent(id);
    }

    public ResponseEntity<Error> deleteAllProduct() {
        shopRepository.deleteAll();
        statisticRepository.deleteAll();
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    public ShopUnitStatisticResponse getSales(String date) {
        List<ShopUnitStatisticUnit> shops24 = new ArrayList<ShopUnitStatisticUnit>();
        Date currentDate = validDateTIme(date);
        Date leftDate = new Date(currentDate.getTime() - (1000 * 60 * 60 * 24));
        Iterable<Shop> all = shopRepository.findAll();
        Iterator<Shop> shopsIter = all.iterator();
        while (shopsIter.hasNext()) {
            Shop shop = shopsIter.next();
            Date dateShop = validDateTIme(shop.getDateF());
            if(dateShop.compareTo(leftDate)>=0 && dateShop.compareTo(currentDate)<=0) {
                shops24.add(new ShopUnitStatisticUnit(shop));
            }
        }
        ShopUnitStatisticResponse answer = new ShopUnitStatisticResponse();
        answer.setItems(shops24);
        return answer;
    }

    public ShopUnitStatisticResponse getStatsOfProduct(String id, String dateStart, String dateEnd) {
        Date d1 = validDateTIme(dateStart);
        Date d2 = validDateTIme(dateEnd);
        if(d1.compareTo(d2)>0)
            throw new ErrorException400("Validation Failed");
        List<ShopStatistic> statisticList = statisticRepository.findAllByUuid(id);
//        log.info("len statistic " + id+" : "+statisticList.size());
        long summa = 0;
        if(statisticList.size()==0)
            throw new ErrorException404("Item not found");
        for (ShopStatistic shopStatistic : statisticList) {
            summa += shopStatistic.getPrice();
        }
//        log.info("summa " + id+" : "+summa);
        ShopUnitStatisticResponse answer = new ShopUnitStatisticResponse();
        List<ShopUnitStatisticUnit> shopsStats = new ArrayList<ShopUnitStatisticUnit>();
        ShopUnitStatisticUnit shopUnitStatisticUnit = new ShopUnitStatisticUnit(shopRepository.findById(id).get());
        shopUnitStatisticUnit.setPrice(summa/statisticList.size());
        shopsStats.add(shopUnitStatisticUnit);
        answer.setItems(shopsStats);
        return answer;
    }
}
