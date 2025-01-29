package com.team03.repository.business;

import com.team03.entity.business.AdvertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertsTypeRepository extends JpaRepository<AdvertType, Long> {

    @Query("SELECT at FROM AdvertType at WHERE LOWER(at.title) = LOWER(:title)")
    AdvertType findByTitle(String title);

    int countAdvertTypeByBuiltInTrue();

    @Query("select c from AdvertType c where c.builtIn = ?1")
    List< AdvertType> findAllByBuiltIn(boolean b);


}
