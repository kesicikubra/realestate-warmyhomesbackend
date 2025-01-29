package com.team03.entity.business;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    //    @JsonIgnore
    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
    @JsonProperty("states")
    private List<City> cities;

}
