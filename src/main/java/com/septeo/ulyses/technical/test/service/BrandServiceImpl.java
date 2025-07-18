package com.septeo.ulyses.technical.test.service;

import com.septeo.ulyses.technical.test.cache.SimpleCache;
import com.septeo.ulyses.technical.test.entity.Brand;
import com.septeo.ulyses.technical.test.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of the BrandService interface.
 * This class provides the implementation for all brand-related operations.
 */
@Service
@Transactional(readOnly = false)
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private SimpleCache<Long, Brand> brandCache;

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Brand> getAllBrands() {
        Map<Long, Brand> cachedBrands = brandCache.getAllOrLoad(() -> {
            List<Brand> brands = brandRepository.findAll();
            return brands.stream().collect(Collectors.toMap(Brand::getId, b -> b));
        });
        return new ArrayList<>(cachedBrands.values());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Brand> getBrandById(Long id) {
        Brand brand = brandCache.getOneOrLoad(id, () ->
                brandRepository.findById(id).orElse(null)
        );
        return Optional.ofNullable(brand);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Brand saveBrand(Brand brand) {

        Brand savedBrand = brandRepository.save(brand);
        if (savedBrand != null && savedBrand.getId() != null) {
            brandCache.put(savedBrand.getId(), savedBrand);
        }
        return savedBrand;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteBrand(Long id) {
        brandRepository.deleteById(id);
        brandCache.remove(id);
    }
}
