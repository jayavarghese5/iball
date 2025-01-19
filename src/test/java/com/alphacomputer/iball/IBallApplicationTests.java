package com.alphacomputer.iball;

import com.alphacomputer.iball.controller.ProductController;
import com.alphacomputer.iball.dto.ProductDto;
import com.alphacomputer.iball.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class IBallApplicationTests {

	@Autowired
    private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;

	@Test
	public void addProduct() {
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("1", "mobile", 2, 1000.0));
		when(productService.saveProduct(productDtoMono)).thenReturn(productDtoMono);
		webTestClient.post().uri("/products")
				.body(productDtoMono, ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void getProductTest(){
		Flux<ProductDto> productDtoFlux = Flux.just(new ProductDto("1", "mobile", 2, 1000.0)
		,new ProductDto("2", "laptop", 2, 1000.0));
		when(productService.getProduct("1")).thenReturn(Mono.just(new ProductDto("1", "mobile", 2, 1000.0)));
		when(productService.getProducts()).thenReturn(productDtoFlux);

		Flux<ProductDto> responseBody=webTestClient.get().uri("/products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("1", "mobile", 2, 1000.0))
				.expectNext(new ProductDto("2", "laptop", 2, 1000.0))
				.verifyComplete();
	}


	@Test
	public void updateProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("1", "mobile", 2, 1000.0));
		when(productService.updateProduct(productDtoMono,"1")).thenReturn(productDtoMono);
		webTestClient.put().uri("/products/update/1")
				.body(productDtoMono, ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void deleteProductTest(){
		Mono<Void> voidReturn = Mono.empty();
		when(productService.deleteProduct("1")).thenReturn(voidReturn);
		webTestClient.delete().uri("/products/delete/1")
				.exchange()
				.expectStatus().isOk();
	}

}
