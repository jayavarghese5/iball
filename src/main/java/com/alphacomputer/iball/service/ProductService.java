package com.alphacomputer.iball.service;

import com.alphacomputer.iball.entity.Product;
import com.alphacomputer.iball.repository.ProductRepository;
import com.alphacomputer.iball.dto.ProductDto;
import com.alphacomputer.iball.util.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    /**
     * Returns the list of Products
     * @return
     */
    public Flux<ProductDto> getProducts(){
        return  productRepository.findAll().map(ProductMapper::entityToDto);
    }

    /**
     * Returns a single Product
     * @param id
     * @return
     */
    public Mono<ProductDto> getProduct(String id){
        return productRepository.findById(id).map(ProductMapper::entityToDto);
    }

    /**
     * gets product in range
     * @param min
     * @param max
     * @return
     */
    public Flux<ProductDto> getProductInRange(double min,double max){
        return productRepository.findByPriceBetween(Range.closed(min,max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
       return productDtoMono.map(ProductMapper::dtoToEntity).flatMap(productRepository::insert)
                .map(ProductMapper::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono,String id){
        return productRepository.findById(id)
                .flatMap(p->productDtoMono
                .map(ProductMapper::dtoToEntity)
                .doOnNext(e->e.setId(id)))
                .flatMap(productRepository::save)
                .map(ProductMapper::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return productRepository.deleteById(id);
    }
}
