package com.team03.payload.request.abstracts;

import com.team03.entity.business.Location;
import com.team03.payload.request.business.PropertyRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseAdvertRequest implements Serializable {
    @NotNull(message = "{base.advert.title.notnull}")
    @Size(min = 5, max = 150, message = "{base.advert.title.size}")
    private String title;

    @Size(max = 500, message = "{base.advert.description.size}")
    private String description;

    @NotNull(message = "{base.advert.price}")
    private Double price;

    @NotNull(message = "{base.advert.location}")
    private Location location;

    private String address;

    @NotNull(message = "{base.advert.advert.type.id}")
    private Long advert_type_id;

    @NotNull(message = "{base.advert.country.id}")
    private Long country_id;

    @NotNull(message = "{base.advert.city.id}")
    private Long city_id;

    @NotNull(message = "{base.advert.district.id}")
    private Long district_id;

    @NotNull(message = "{base.advert.category.id}")
    private Long category_id;

    @NotNull(message = "{base.advert.properties}")
    private List<PropertyRequest> properties;
}
