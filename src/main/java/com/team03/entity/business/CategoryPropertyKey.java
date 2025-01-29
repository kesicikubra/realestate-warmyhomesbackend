package com.team03.entity.business;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.entity.enums.KeyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "category_property_keys")
public class CategoryPropertyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 80, message = "{category.property.key.name.size}")
    private String name;

    @Enumerated(EnumType.STRING)
    private KeyType keyType;

    @JsonIgnore
    private Boolean builtIn =false;

    private String unit;

}

