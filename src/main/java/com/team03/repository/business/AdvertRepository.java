package com.team03.repository.business;

import com.team03.entity.business.Advert;
import com.team03.entity.enums.AdvertStatus;
import com.team03.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    @Query("SELECT c.name,c.id, COUNT(a) FROM Advert a INNER JOIN a.city c GROUP BY c.name,c.id")
    List<Object[]> findCityAndAdvertCount();

    @Query("SELECT a.category, a.category.icon, a.category.title, COUNT(a) " +
            "FROM Advert a " +
            "GROUP BY a.category")
    List<Object[]> findAdvertsCountByCategory();

    @Query("SELECT a FROM Advert a WHERE a.user.email = :email")
    Page<Advert> findAuthenticatedUserAdverts(@Param("email") String email, Pageable pageable);

    Optional<Advert> findByIdAndUser(Long id, User user);

    @Query("SELECT a FROM Advert a WHERE a.slug = :slug AND a.status = AdvertStatus.ACTIVATED")
    Optional<Advert> findBySlug(@Param("slug") String slug);

    @Query("SELECT a FROM Advert a " +
            "WHERE (COALESCE(:q, '') = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "AND (:category_id IS NULL OR a.category.id = :category_id) " +
            "AND (a.category.isActive = true) " +
            "AND (a.isActive = true) " +
            "AND (a.status = AdvertStatus.ACTIVATED) " +
            "AND (:advert_type_id IS NULL OR a.advertType.id = :advert_type_id) " +
            "AND (:price_start IS NULL OR a.price >= :price_start) " +
            "AND (:price_end IS NULL OR a.price <= :price_end) " +
            "AND (:city_id IS NULL OR a.city.id = :city_id)"
    )
    Page<Advert> findAdvertsByCriteria(
            @Param("q") String query,
            @Param("category_id") Long categoryId,
            @Param("advert_type_id") Long advertTypeId,
            @Param("price_start") Double priceStart,
            @Param("price_end") Double priceEnd,
            @Param("city_id") Long cityId,
            Pageable pageable
    );

    @Query("SELECT a FROM Advert a " +
            "WHERE (COALESCE(:q, '') = '' OR LOWER(a.title) LIKE LOWER(CONCAT('%', :q, '%')) OR " +
            "LOWER(a.description) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "AND (:category_id IS NULL OR a.category.id = :category_id)" +
            "      AND (:advert_type_id IS NULL OR a.advertType.id = :advert_type_id)" +
            "      AND (:status IS NULL OR a.status = :status)"
    )
    Page<Advert> findAdvertsByCriteriaWithStatus(
            @Param("q") String query,
            @Param("category_id") Long categoryId,
            @Param("advert_type_id") Long advertTypeId,
            @Param("status") AdvertStatus status,
            Pageable pageable
    );

    @Query("SELECT a FROM Advert a WHERE a.status = AdvertStatus.ACTIVATED ORDER BY (3 * SIZE(a.tourRequests) + a.viewCount) DESC")
    List<Advert> findMostPopularAdverts(Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Advert a WHERE a.category.id = :categoryId")
    Boolean existsByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT COUNT(a) > 0 FROM Advert a WHERE a.user.id = :userId")
    Boolean existsByUserId(@Param("userId") Long userId);

    @Query("SELECT (COUNT(a) > 0) FROM Advert a WHERE a.user.id = :userId AND a.slug = :slug")
    boolean existsSlugByUserId(@Param("userId") Long userId, @Param("slug") String slug);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Advert a WHERE a.advertType.id = :advertTypeId")
    Boolean existsByAdvert_typeId(@Param("advertTypeId") Long advertTypeId);

    @Query("SELECT a FROM Advert a " +
            "WHERE EXTRACT(DATE FROM a.createAt) BETWEEN :beginDate AND :endDate " +
            "AND (COALESCE(:categoryTitle, '') = '' OR a.category.title = :categoryTitle) " +
            "AND (COALESCE(:typeTitle, '') = '' OR a.advertType.title = :typeTitle) " +
            "AND (COALESCE(:status, '') = '' OR a.status = :status)")
    List<Advert> findAdvertsBetweenDatesAndFilters(@Param("beginDate") LocalDate beginDate,
                                                   @Param("endDate") LocalDate endDate,
                                                   @Param("categoryTitle") String categoryTitle,
                                                   @Param("typeTitle") String typeTitle,
                                                   @Param("status") AdvertStatus status);

    @Query("SELECT a " +
            "FROM Advert a " +
            "LEFT JOIN a.tourRequests tr " +
            "WHERE a.id IN (SELECT DISTINCT tr.advert.id FROM TourRequest tr) " +
            "ORDER BY SIZE(a.tourRequests) DESC " +
            "LIMIT :numberOfAdverts")
    List<Advert> findAdvertsWithMostTourRequests(@Param("numberOfAdverts") Integer numberOfAdverts);


    @Query("SELECT a FROM Advert a WHERE a.user.id = :id ")
    Page< Advert> findByUserId(Long id,Pageable pageable);

    int countAdvertByBuiltInTrue();


    @Query("SELECT a FROM Advert a WHERE a.builtIn = ?1")
    List<Advert> findAllByBuiltIn(boolean b);
}
