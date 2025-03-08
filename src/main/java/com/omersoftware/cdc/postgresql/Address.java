package com.omersoftware.cdc.postgresql;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
record Address(
    @NotBlank String street, @NotBlank String city, @NotBlank String state, @NotBlank String zip) {}
