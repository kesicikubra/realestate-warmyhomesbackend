package com.team03.entity.business;


import jakarta.persistence.*;
import lombok.*;


@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] imageData;

    @Column(nullable = false)
    private String name;

    private String type;

    @Column(nullable = false)
    private Boolean featured;

    @ManyToOne
    private Advert advert;

    private Boolean suitable=true;


}
