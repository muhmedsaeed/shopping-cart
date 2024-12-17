package com.mosaeed.shoppingcart.service.product;

import com.mosaeed.shoppingcart.dto.ProductDto;
import com.mosaeed.shoppingcart.model.Product;
import com.mosaeed.shoppingcart.request.AddProductRequest;
import com.mosaeed.shoppingcart.request.UpdateProductRequest;

import java.util.List;

public interface IProductService {

    Product addProduct(AddProductRequest productRequest);
    Product updateProduct(UpdateProductRequest request, Long id);
    void deleteProductById(Long id);

    Product getProductById(Long id);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductsByBrand(String brand);
    List<Product> getProductsByCategoryAndBrand(String category, String brand);
    List<Product> getProductsByName(String name);
    List<Product> getProductsByBrandAndName(String brand, String name);
    Long countProductsByBrandAndName(String brand, String name);

    ProductDto convertToDto(Product product);

    List<ProductDto> getConvertedProducts(List<Product> products);
}
