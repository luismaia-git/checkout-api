package com.qikserve.checkout.multitenancy.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity(name = "tenants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@With
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @NotNull
    @Column(name = "mappers", nullable = false)
    private String mappers;
}
