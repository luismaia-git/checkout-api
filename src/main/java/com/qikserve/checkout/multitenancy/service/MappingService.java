package com.qikserve.checkout.multitenancy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qikserve.checkout.model.dto.product.Product;
import com.qikserve.checkout.model.dto.promotion.base.AbstractPromotion;
import com.qikserve.checkout.multitenancy.model.dto.TenantProductDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MappingService {

    private final ObjectMapper objectMapper;

    public MappingService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public List<TenantProductDTO> mapAllProducts(String mappersJson, List<Map<String, Object>> rawProducts) {
        Map<String, Object> mappers = parseMappersJson(mappersJson);

        Map<String, String> productMapper = (Map<String, String>) mappers.get("product");
        List<TenantProductDTO> products = new ArrayList<>();

        for (Map<String, Object> rawProduct : rawProducts) {
            String id = (String) rawProduct.get(productMapper.get("id"));
            String name = (String) rawProduct.get(productMapper.get("name"));
            BigInteger price = new BigInteger(String.valueOf(rawProduct.get(productMapper.get("price"))));

            TenantProductDTO product = TenantProductDTO.builder()
                    .id(id)
                    .name(name)
                    .price(price)
                    .build();

            products.add(product);
        }

        return products;
    }

    public Product mapProductWithPromotions(String mappersJson, Map<String, Object> rawProduct) {
        Map<String, Object> mappers = parseMappersJson(mappersJson);

        Map<String, String> productMapper = (Map<String, String>) mappers.get("product");
        String id = (String) rawProduct.get(productMapper.get("id"));
        String name = (String) rawProduct.get(productMapper.get("name"));
        BigInteger price = new BigInteger(String.valueOf(rawProduct.get(productMapper.get("price"))));

        List<Map<String, Object>> rawPromotions = (List<Map<String, Object>>) rawProduct.get("promotions");
        List<AbstractPromotion> promotions = new ArrayList<>();
        if (rawPromotions != null) {
            for (Map<String, Object> rawPromotion : rawPromotions) {
                AbstractPromotion promotion = mapPromotion(rawPromotion);
                promotions.add(promotion);
            }
        }

        return new Product(id, name, price, promotions);
    }

    private AbstractPromotion mapPromotion(Map<String, Object> rawPromotion) {
        try {

            String json = objectMapper.writeValueAsString(rawPromotion);
            return objectMapper.readValue(json, AbstractPromotion.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid promotion data", e);
        }
    }

    private Map<String, Object> parseMappersJson(String mappersJson) {
        try {
            return objectMapper.readValue(mappersJson, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid mappers JSON", e);
        }
    }

}