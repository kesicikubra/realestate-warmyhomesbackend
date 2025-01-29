package com.team03.repository.business;


import com.team03.entity.business.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT (count(c) > 0) FROM City c JOIN c.districts d WHERE c.id = :cityId AND d.id = :districtId")
    boolean existsByDistrictId(@Param("cityId") Long cityId, @Param("districtId") Long districtId);

}
