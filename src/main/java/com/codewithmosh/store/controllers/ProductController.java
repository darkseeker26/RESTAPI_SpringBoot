package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
   private final ProductRepository productRepository;
   private final ProductMapper productMapper;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping
   public List<ProductDto> getAllProducts(
          @RequestParam(name="categoryId", required = false) Byte categoryId
   ) {

       List<Product> products;
       if (categoryId != null) {
           products = productRepository.findAllByCategoryId(categoryId);
       }
       else {
           products = productRepository.findWithCategory();
       }

       return products
               .stream()
               .map(productMapper::toDto)
               .toList();
   }

   @PostMapping
   public ResponseEntity<ProductDto> createProduct(
           @RequestBody ProductDto request,
           UriComponentsBuilder uriBuilder
   ){
       var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
       if (category == null) {
           return ResponseEntity.badRequest().build();
       }
       var product = productMapper.toEntity(request);
       product.setCategory(category);
       productRepository.save(product);
       request.setId(product.getId());
       var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
       return ResponseEntity.created(uri).body(request);
   }

   @PutMapping("/{id}")
   public ResponseEntity<ProductDto> updateProduct(
           @PathVariable Long id,
           @RequestBody ProductDto request
   ){
       var category = categoryRepository.findById(request.getCategoryId()).orElse(null);
       if(category == null) {
           return ResponseEntity.badRequest().build();
       }
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(request,product);
        product.setCategory(category);
        productRepository.save(product);
        request.setId(product.getId());
        return ResponseEntity.ok(request);
   }

   @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable(name="id") Long id
   ){
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
   }
}
