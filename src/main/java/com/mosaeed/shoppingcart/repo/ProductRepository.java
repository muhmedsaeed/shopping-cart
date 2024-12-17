package com.mosaeed.shoppingcart.repo;

import com.mosaeed.shoppingcart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // nothing to do

    @Query("""
        SELECT p FROM Product p
        WHERE p.category.name = :name
        """)
    List<Product> findByCategoryName(String name);

    
    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByName(String name);

    List<Product> findByBrandAndName(String brand, String name);

    Long countByBrandAndName(String brand, String name);

}
