package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.entity.Vehicle;
import com.septeo.ulyses.technical.test.repository.SalesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Implementation of the SalesService interface.
 * This class provides the implementation for all sales-related operations.
 */
@Service
@Transactional(readOnly = false)
public class SalesServiceImpl implements SalesService {

    @Autowired
    private SalesRepository salesRepository;
    @Autowired
    private VehicleService vehicleService;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sales> getAllSales(Pageable pageable) {
        return salesRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Sales> getSalesById(Long id) {
        return salesRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sales> getSalesByBrand(Long brandId) {
        return salesRepository.findByBrand(brandId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Sales> getSalesByVehicle(Long vehicleId) {
        return salesRepository.findByVehicle(vehicleId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Vehicle> getBestSellingVehicles(LocalDate startDate, LocalDate endDate) {
        List<Sales> salesList = salesRepository.findAll();
        HashMap<Long, Integer> vehicleSalesCount = new HashMap<>();
        final int MAX_VEHICLES = 5;

        LocalDate finalStartDate = (startDate != null) ? startDate : LocalDate.MIN.plusDays(1);
        LocalDate finalEndDate = (endDate != null) ? endDate : LocalDate.MAX.minusDays(1);
        salesList.stream()
                .filter(sale -> sale.getSaleDate().isAfter(finalStartDate.minusDays(1)) &&
                                sale.getSaleDate().isBefore(finalEndDate.plusDays(1)))
                .forEach(sale -> {
                    Vehicle vehicle = sale.getVehicle();
                    vehicleSalesCount.put(vehicle.getId(), vehicleSalesCount.getOrDefault(vehicle.getId(), 0) + 1);
                });
        
        Queue<Long> heap = new PriorityQueue<>(
                (n1, n2) -> vehicleSalesCount.get(n1) - vehicleSalesCount.get(n2));
        for (Long vehicleId : vehicleSalesCount.keySet()) {
            heap.add(vehicleId);
            if (heap.size() > MAX_VEHICLES) heap.poll();
        }

        List<Vehicle> bestSellingVehicles = new ArrayList<>();
        for (int i = MAX_VEHICLES - 1; i >= 0; i--) {
            Long vehicleId = heap.poll();
            vehicleService.getVehicleById(vehicleId).ifPresent(bestSellingVehicles::addFirst);
        }

        return bestSellingVehicles;
    }
}
