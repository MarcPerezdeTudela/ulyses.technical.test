package com.septeo.ulyses.technical.test.repository;

import com.septeo.ulyses.technical.test.entity.Sales;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Sales entity.
 */
@Repository
public interface SalesRepository {
    /**
     * Find all sales.
     *
     * @return a list of all sales
     */
    List<Sales> findAll(Pageable pageable);
    List<Sales> findAll();

    /**
     * Find a sale by its ID.
     *
     * @param id the ID of the sale to find
     * @return an Optional containing the sale if found, or empty if not found
     */
    Optional<Sales> findById(Long id);

    /**
     * Find sales by brand ID.
     *
     * @param brandId the brand to filter sales by
     * @return a list of sales associated with the given brand
     */
    List<Sales> findByBrand(Long brandId);

    /**
     * Find sales by vehicle ID.
     *
     * @param vehicleId the vehicle to filter sales by
     * @return a list of sales associated with the given vehicle
     */
    List<Sales> findByVehicle(Long vehicleId);

}
