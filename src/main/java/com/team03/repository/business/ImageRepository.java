package com.team03.repository.business;

import com.team03.entity.business.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface ImageRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.advert.id = :advertId")
    List<Image> findByAdvertId(@Param("advertId") Long advertId);

    @Modifying
    @Query("DELETE FROM Image i WHERE i.id IN :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);

    @Query("SELECT i FROM Image i WHERE i.advert.id = :advertId AND i.featured = true")
    List<Image> findByAdvertIdAndIsFeaturedTrue(@Param("advertId") Long advertId);
}
