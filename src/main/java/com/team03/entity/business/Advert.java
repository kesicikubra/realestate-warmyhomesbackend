package com.team03.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.team03.entity.enums.AdvertStatus;
import com.team03.entity.listener.AdvertEntityListener;
import com.team03.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "adverts")
@EntityListeners(AdvertEntityListener.class)
public class Advert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 150,message = "{advert.title.size}")
    @Column(nullable = false, length = 150)
    private String title;

    private String description;

    @Size(min = 5, max = 200)
    @Column(nullable = false, length = 300, unique = true)
    private String slug;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AdvertStatus status= AdvertStatus.PENDING;

    @Column(nullable = false)
    private Boolean builtIn;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Integer viewCount;

    @Column(nullable = false)
    @Embedded
    private Location location;

    private String address;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "US")
    private LocalDateTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "US")
    private LocalDateTime updateAt;


    @ManyToOne
    @JoinColumn(nullable = false)
    private AdvertType advertType;

    @ManyToOne
    @JoinColumn(nullable = false, name = "country_id")
    private Country country;

    @ManyToOne
    @JoinColumn(nullable = false, name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(nullable = false, name = "district_id")
    private District district;


    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL )
    private List<Image> images;

    @JsonIgnore
    @OneToMany(mappedBy = "advert", cascade = CascadeType.REMOVE)
    private List<Favorite> favorites;

    @JsonIgnore
    @OneToMany(mappedBy = "advert", cascade = CascadeType.REMOVE)
    private List<Log> logs;

    @JsonIgnore
    @OneToMany(mappedBy = "advert", cascade = CascadeType.REMOVE)
    private List<CategoryPropertyValue> categoryPropertyValues;

    @JsonIgnore
    @OneToMany(mappedBy = "advert", cascade = CascadeType.REMOVE)
    private List<TourRequest> tourRequests;

    @JsonIgnore
    @OneToMany(mappedBy = "advert", cascade = CascadeType.ALL)
    private Set<DailyAdvertView> views;

}
