package com.alphacomputer.iball.controller;

import com.alphacomputer.iball.dto.ProductDto;
import com.alphacomputer.iball.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Flux<ProductDto>> getProducts(){
        return ResponseEntity.ok().body(productService.getProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<ProductDto>> getProduct(@PathVariable String id){
        return ResponseEntity.ok().body(productService.getProduct(id));
    }

    @GetMapping("/product-range")
    public ResponseEntity<Flux<ProductDto>> getProductBetweenRange(@RequestParam("min") double min,@RequestParam("max") double max){
        return ResponseEntity.ok().body(productService.getProductInRange(min,max));
    }

    @PostMapping
    public ResponseEntity<Mono<ProductDto>> saveProduct(@RequestBody Mono<ProductDto> productDtoMono){
        return  ResponseEntity.ok().body(productService.saveProduct(productDtoMono));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Mono<ProductDto>> updateProduct(@RequestBody Mono<ProductDto> productDtoMono,@PathVariable String id){
        return  ResponseEntity.ok().body(productService.updateProduct(productDtoMono,id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Mono<Void>> deleteProduct(@PathVariable String id) {
          return  ResponseEntity.ok().body(productService.deleteProduct(id));
    }
}
