package org.acme.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class FindProductModel {
    private String name;
    private Double minPrice;
    private Double maxPrice;
}
