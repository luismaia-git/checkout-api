package com.qikserve.checkout.repository.product;

import com.qikserve.checkout.exception.product.notfound.ProductNotFoundException;
import com.qikserve.checkout.exception.wiremock.WiremockUnavailableException;
import com.qikserve.checkout.model.dto.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Repository
public class WiremockProductRepository implements ProductRepository {

    private final RestTemplate restTemplate;

    @Autowired
    public WiremockProductRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private <T> T get(String url, Class<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.getForEntity(url, responseType);
            return response.getBody();
        } catch (ResourceAccessException exception) {
            throw WiremockUnavailableException.of(url);
        }
    }

    @Override
    public Product findById(String productId, String baseUrl) {
        String url = baseUrl + "/products/" + productId;

        try {
            return get(url, Product.class);
        }
        catch (HttpClientErrorException e) {
            throw ProductNotFoundException.of(productId);
        }
    }

    @Override
    public List<Product> findProductsByIds(List<String> productsIds, String baseUrl) {
        List<Product> products = Arrays.asList(get(baseUrl + "/products", Product[].class));
        return products.stream()
                .filter(product -> productsIds.contains(product.getId()))
                .toList();
    }

    @Override
    public List<Product> findAll(String baseUrl) {
        return Arrays.asList(get(baseUrl + "/products", Product[].class));
    }
}