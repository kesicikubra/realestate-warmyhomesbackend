package com.team03.repository.business;

import com.team03.entity.business.Category;
import com.team03.entity.business.CategoryPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPropertyKeyRepository extends JpaRepository<CategoryPropertyKey, Long> {

    @Query("SELECT c FROM Category c JOIN c.categoryPropertyKeys cpk WHERE cpk.id = :keyId")
    Category findByCategoryPropertyKeyId(@Param("keyId") Long keyId);
}
