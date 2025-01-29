package com.team03.repository.business;


import com.team03.entity.business.TourRequest;
import com.team03.entity.enums.TourReqStatus;
import com.team03.entity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TourRequestRepository extends JpaRepository<TourRequest, Long > {
    @Query("SELECT t FROM TourRequest t WHERE t.advert.id = :advertId AND t.tourReqStatus = :status")
    List<TourRequest> findAllByAdvert(@Param("advertId") Long advertId, @Param("status") TourReqStatus status);

    @Query("SELECT t FROM TourRequest t WHERE t.guestUser = :user OR t.ownerUser = :user")
    List<TourRequest> findAllByGuestUserAndOwnerUser(@Param("user") User user);

    @Query("SELECT t FROM TourRequest t WHERE t.guestUser = :user AND t.advert.id = :advertId")
    List<TourRequest> findAllByGuestUserAndAdvert(@Param("user") User user, @Param("advertId") Long advertId);

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM TourRequest t WHERE t.guestUser = :user AND t.advert.id = :advertId")
    boolean existsByGuestUserAndAdvert(@Param("user") User user, @Param("advertId") Long advertId);

    @Query("SELECT t FROM TourRequest t WHERE (:q IS NULL OR " +
            "(t.advert.title LIKE CONCAT('%', :q, '%') OR " +
            "t.advert.description LIKE CONCAT('%', :q, '%'))) " +
            "AND (t.guestUser = :user OR t.ownerUser = :user)")
    Page<TourRequest> findAllByAuthenticatedPage(Pageable pageable, @Param("user") User user, @Param("q") String q);

    @Query("SELECT t FROM TourRequest t WHERE (:q IS NULL OR " +
            "(t.advert.title LIKE CONCAT('%', :q, '%') OR " +
            "t.advert.description LIKE CONCAT('%', :q, '%'))) OR '' = :q")
    Page<TourRequest> findAllTourReqByPageForAdminAndManager(Pageable pageable, @Param("q") String q);

    @Query("SELECT (COUNT(t) > 0) FROM TourRequest t INNER JOIN t.ownerUser o INNER JOIN t.guestUser g WHERE o.id = :id OR g.id = :id")
    Boolean existsByUserId(Long id);

    @Query("SELECT t FROM TourRequest t " +
            "WHERE EXTRACT(DATE FROM t.createAt) BETWEEN :beginDate AND :endDate " +
            "AND (COALESCE(:status, '') = '' OR t.tourReqStatus = :status)")
    List<TourRequest> findTourRequestsBetweenDatesAndStatus(@Param("beginDate") LocalDate beginDate,
                                                            @Param("endDate") LocalDate endDate,
                                                            @Param("status") TourReqStatus tourReqStatus);


    @Query("SELECT t FROM TourRequest t WHERE (:q IS NULL OR " +
            "(t.advert.title LIKE CONCAT('%', :q, '%') OR " +
            "t.advert.description LIKE CONCAT('%', :q, '%'))) " +
            "AND t.ownerUser = :user")
    Page<TourRequest> findAllByAuthenticatedOwnerPage(Pageable pageable, @Param("user") User user, @Param("q") String q);

    @Query("SELECT t FROM TourRequest t WHERE (:q IS NULL OR " +
            "(t.advert.title LIKE CONCAT('%', :q, '%') OR " +
            "t.advert.description LIKE CONCAT('%', :q, '%'))) " +
            "AND t.guestUser = :user ")
    Page<TourRequest> findAllByAuthenticatedGuestPage(Pageable pageable, @Param("user") User user, @Param("q") String q);


    @Query("SELECT t FROM TourRequest t WHERE t.guestUser = :user OR t.ownerUser = :user")
    Page<TourRequest> findAllByGuestUserAndOwnerUser(@Param("user") User user,Pageable pageable);


}
