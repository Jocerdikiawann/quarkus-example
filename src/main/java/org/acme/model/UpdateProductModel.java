package org.acme.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateProductModel {
    private String name;
    private String description;
    private BigDecimal price;
}
