package com.septeo.ulyses.technical.test.controller;

import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.entity.Vehicle;
import com.septeo.ulyses.technical.test.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final int PAGE_SIZE = 10;
    @Autowired
    private SalesService salesService;

    @GetMapping
    public ResponseEntity<List<Sales>> getAllSales(@RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return ResponseEntity.ok(salesService.getAllSales(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<Sales> getSalesById(@PathVariable Long id) {
        return salesService.getSalesById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/brands/{brandId}")
    public ResponseEntity<List<Sales>> getSalesByBrand(@PathVariable Long brandId) {
        List<Sales> salesList = salesService.getSalesByBrand(brandId);
        if (salesList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(salesList);
    }

    @GetMapping("/vehicles/{vehicleId}")
    public ResponseEntity<List<Sales>> getSalesByVehicle(@PathVariable Long vehicleId) {
        List<Sales> salesList = salesService.getSalesByVehicle(vehicleId);
        if (salesList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(salesList);
    }

    @GetMapping("/vehicles/bestSelling")
    public ResponseEntity<List<Vehicle>> getBestSellingVehicles(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(salesService.getBestSellingVehicles(startDate, endDate));
    }
}
