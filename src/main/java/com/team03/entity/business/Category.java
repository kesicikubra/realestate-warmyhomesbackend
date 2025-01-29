package com.team03.entity.business;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String icon;

    @Column(nullable = false)
    private Boolean builtIn = false;

    private Integer seq;

    private String slug;

    @Column(nullable = false, name = "is_active")
    private Boolean isActive ;

    @Column(nullable = false, name = "create_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "US")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "US")
    private LocalDateTime updateAt;

    @OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true)
    @JoinColumn(name = "category_id")
    private Set<CategoryPropertyKey> categoryPropertyKeys;




}
