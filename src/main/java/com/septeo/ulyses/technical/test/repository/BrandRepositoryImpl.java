package com.septeo.ulyses.technical.test.repository;

import com.septeo.ulyses.technical.test.entity.Brand;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the BrandRepository interface.
 * This class provides the implementation for all brand-related database operations.
 */
@Repository
public class BrandRepositoryImpl implements BrandRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Brand> findAll() {
        String stringQuery = "SELECT b FROM Brand b";
        Query query = entityManager.createQuery(stringQuery);
        try {
            // Simulating a delay to mimic a long-running query just to test cache system, this is not recommended for production code.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Brand> findById(Long id) {
        String stringQuery = "SELECT b FROM Brand b WHERE b.id = :id";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("id", id);
        try {
            // Simulating a delay to mimic a long-running query just to test cache system, this is not recommended for production code.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            return Optional.of((Brand) query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brand save(Brand brand) {
        if (brand.getId() == null) {
            entityManager.persist(brand);
            return brand;
        } else {
            return entityManager.merge(brand);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteById(Long id) {
        String stringQuery = "DELETE FROM Brand b WHERE b.id = :id";
        Query query = entityManager.createQuery(stringQuery);
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
