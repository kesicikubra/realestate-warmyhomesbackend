package com.team03.entity.business;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "category_property_values")
public class CategoryPropertyValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 100)
    private String value;

    @ManyToOne
    private Advert advert;

    @ManyToOne
    private CategoryPropertyKey categoryPropertyKey;

}
