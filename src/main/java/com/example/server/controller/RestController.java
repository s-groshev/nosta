package com.example.server.controller;


import com.example.server.entity.Shop;
import com.example.server.exeption.Error;
import com.example.server.exeption.ErrorException400;
import com.example.server.exeption.ErrorException404;
import com.example.server.schemas.ShopUnit;
import com.example.server.schemas.ShopUnitImportRequest;
import com.example.server.schemas.ShopUnitStatisticResponse;
import com.example.server.service.ShopService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
public class RestController {

    ShopService shopService;

    @GetMapping("/parent/{id}")
    public List<Shop> getAllByParent(@PathVariable("id") String id) {
        return shopService.getAllByParent(id);
    }

    @PostMapping(value = "/imports")
    public ResponseEntity<Error> postProducts(@RequestBody String JSON) throws JsonProcessingException {
        JSON = JSON.replaceAll("None","null");
        ObjectMapper objectMapper =new ObjectMapper();
        ShopUnitImportRequest products = objectMapper.readValue(JSON,ShopUnitImportRequest.class);
        return shopService.postProducts(products);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Error> deleteProduct(@PathVariable("id") String id) {
        return shopService.deleteProduct(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Error> deleteAllProduct() {
        return shopService.deleteAllProduct();
    }

    @GetMapping("/nodes/{id}")
    public ShopUnit getProduct(@PathVariable("id") String id) {
        return shopService.getProduct(id);
    }

    @GetMapping("/sales")
    public ShopUnitStatisticResponse getSales(@RequestParam("date") String date) {
        return shopService.getSales(date);
    }

    @GetMapping("/node/{id}/statistic")
    public ShopUnitStatisticResponse getStatsOfProduct(
            @PathVariable("id") String id,
            @RequestParam("dateStart") String dateStart,
            @RequestParam("dateEnd") String dateEnd){
        return shopService.getStatsOfProduct(id,dateStart,dateEnd);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handlerException400(ErrorException400 exception){
        Error error = new Error();
        error.setMessage(exception.getMessage());
        error.setCode(400);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Error> handlerException404(ErrorException404 exception){
        Error error = new Error();
        error.setMessage(exception.getMessage());
        error.setCode(404);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
