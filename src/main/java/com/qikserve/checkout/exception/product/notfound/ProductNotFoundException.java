package com.qikserve.checkout.exception.product.notfound;

import com.qikserve.checkout.exception.product.base.ProductException;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuperBuilder
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends ProductException {

    public static ProductNotFoundException of(String productId) {
        return ProductNotFoundException.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .messageCode("error.productIdNotFound")
                .productId(productId)
                .build();
    }

}
