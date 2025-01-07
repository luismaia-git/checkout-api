package com.qikserve.checkout.multitenancy.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@With
public class TenantUpdateDTO {

    private String name;

    private String baseUrl;

    private String mappers;
}
