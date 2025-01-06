package com.qikserve.checkout.multitenancy.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TenantMappersDTO {
    @NotNull
    @NotBlank
    private String mappers;
}
