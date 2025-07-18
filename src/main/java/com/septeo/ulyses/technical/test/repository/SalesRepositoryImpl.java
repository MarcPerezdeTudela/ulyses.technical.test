package com.septeo.ulyses.technical.test.repository;

import com.septeo.ulyses.technical.test.entity.Brand;
import com.septeo.ulyses.technical.test.entity.Sales;
import com.septeo.ulyses.technical.test.entity.Vehicle;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of the SalesRepository interface.
 * This class provides the implementation for all sales-related operations.
 */
@Repository
public class SalesRepositoryImpl implements SalesRepository {

    private final int DEFAULT_PAGE_SIZE = 10;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Sales> findAll(Pageable pageable
    ) {
        String stringQuery = "SELECT s FROM Sales s";
        Query query = entityManager.createQuery(stringQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }

    @Override
    public List<Sales> findAll() {
        String stringQuery = "SELECT s FROM Sales s";
        Query query = entityManager.createQuery(stringQuery);

        return query.getResultList();
    }

    @Override
    public Optional<Sales> findById(Long id) {
        String stringQuery = "SELECT s FROM Sales s WHERE s.id = :id";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("id", id);

        try {
            return Optional.of((Sales) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Sales> findByBrand(Long brandId) {
        String stringQuery = "SELECT s FROM Sales s WHERE s.brand.id = :brandId";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("brandId", brandId);

        return query.getResultList();
    }

    @Override
    public List<Sales> findByVehicle(Long vehicleId) {
        String stringQuery = "SELECT s FROM Sales s WHERE s.vehicle.id = :vehicleId";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("vehicleId", vehicleId);

        return query.getResultList();
    }

}
