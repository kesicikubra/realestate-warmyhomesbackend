package com.team03.repository.business;


import com.team03.entity.business.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("SELECT  (COUNT(c) > 0) FROM Country c JOIN c.cities city WHERE c.id = :countryId AND city.id = :cityId")
    boolean existsByCityId(@Param("countryId") Long countryId, @Param("cityId") Long cityId);
}
