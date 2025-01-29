package com.team03.repository.business;


import com.team03.entity.business.CategoryPropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryPropertyValueRepository extends JpaRepository<CategoryPropertyValue, Long> {


    @Query("SELECT c FROM CategoryPropertyValue c WHERE c.categoryPropertyKey.id = :keyId AND c.advert.id = :advertId")
    Optional<CategoryPropertyValue> findByCategoryPropertyKeyIdAndAdvertId(@Param("keyId") Long keyId,@Param("advertId") Long advertId);
}
