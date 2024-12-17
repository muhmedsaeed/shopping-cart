package com.mosaeed.shoppingcart.service.product;

import com.mosaeed.shoppingcart.dto.ImageDto;
import com.mosaeed.shoppingcart.dto.ProductDto;
import com.mosaeed.shoppingcart.exceptions.ResourceNotFoundException;
import com.mosaeed.shoppingcart.model.Category;
import com.mosaeed.shoppingcart.model.Image;
import com.mosaeed.shoppingcart.model.Product;
import com.mosaeed.shoppingcart.repo.CategoryRepository;
import com.mosaeed.shoppingcart.repo.ImageRepository;
import com.mosaeed.shoppingcart.repo.ProductRepository;
import com.mosaeed.shoppingcart.request.AddProductRequest;
import com.mosaeed.shoppingcart.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor // used for constructor injection but must declare the field as final
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;



    // for get products
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository
                .findById(id).orElseThrow( ()-> new ResourceNotFoundException("Product Not Found!"));
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }


    // for add product
    @Override
    public Product addProduct(AddProductRequest request) {
        /*
        * check if the category id is existing in DB or not
        *   if yes, then set the product in it as new product category
        *   if no , the add it as a new category
        *       then set the product in it as a new product category
        **/
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }
    private Product createProduct(AddProductRequest request, Category category) {

        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category);
    }


    // for update product
    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {

        return productRepository.findById(id)
                .map(existing -> updateExistingProduct(existing, request))
                .map(productRepository::save)
                .orElseThrow(()-> new ResourceNotFoundException("Product Not Found!"));
    }
    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }


    // for delete product
    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(
                productRepository::delete,
                () -> {throw new ResourceNotFoundException("Product Not Found!");});

    }

    // for count the products number
    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }



    // for convert service
    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imagesDto = images
                .stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();

        productDto.setImages(imagesDto);
        return productDto;
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

}
