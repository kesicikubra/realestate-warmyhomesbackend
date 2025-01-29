package com.team03.repository.business;


import com.team03.entity.business.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("SELECT c FROM Category c " +
            "WHERE (:q IS NULL OR :q = '' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "AND c.isActive = true")
    Page<Category> findByQueryIsActiveTrue(
            String q,
            Pageable pageable);


    @Query("SELECT c FROM Category c " +
            "WHERE (:q IS NULL OR :q = '' OR LOWER(c.title) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Category> findByQuery(String q, Pageable pageable);

    @Query("SELECT c FROM Category c " +
            "WHERE c.slug=LOWER(:slug)")
    Category findByCategoriesWithSlug(String slug);

    Boolean existsByTitle(String title);

    int countCategoryByBuiltInTrue();


    @Query("select c from Category c where c.builtIn = ?1")
    List< Category> findAllByBuiltIn(boolean b);
}
