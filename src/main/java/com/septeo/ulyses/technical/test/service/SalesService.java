package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.entity.Brand;
import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.entity.Vehicle;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for Sales operations.
 */
public interface SalesService {

    /**
     * Get all sales.
     *
     * @param pageable the pagination information
     * @return a list of all sales
     */
    List<Sales> getAllSales(Pageable pageable);

    /**
     * Get a sales by its ID.
     *
     * @param id the ID of the sales to find
     * @return an Optional containing the sales if found, or empty if not found
     */
    Optional<Sales> getSalesById(Long id);

    /**
     * Get sales by brand.
     *
     * @param brandId the ID of the brand to filter sales by
     * @return a list of sales associated with the given brand
     */
    List<Sales> getSalesByBrand(Long brandId);

    /**
     * Get sales by vehicle.
     *
     * @param vehicleId the ID of the vehicle to filter sales by
     * @return a list of sales associated with the given vehicle
     */
    List<Sales> getSalesByVehicle(Long vehicleId);

    /**
     * Get the best-selling vehicles within a date range.
     *
     * @param startDate the start date of the range (optional)
     * @param endDate   the end date of the range (optional)
     * @return a list of best-selling vehicles
     */
    List<Vehicle> getBestSellingVehicles(LocalDate startDate, LocalDate endDate);

}
