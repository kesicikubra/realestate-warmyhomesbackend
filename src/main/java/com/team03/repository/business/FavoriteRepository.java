package com.team03.repository.business;

import com.team03.entity.business.Advert;
import com.team03.entity.business.Favorite;
import com.team03.entity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Transactional
    void deleteByUserId(Long id);

    List<Favorite> findByUserId(Long userId);

    @Query("SELECT f FROM Favorite f INNER JOIN f.advert a INNER JOIN f.user u WHERE a.id= :advertId AND u.id = :userId")
    Optional<Favorite> findByAdvertIdAndUserId(@Param("advertId") Long advertId, @Param("userId") Long userId);

    List<Favorite> findAllByUser(User user);

    Page<Favorite> findByUserId(Long userId, Pageable pageable);


    boolean existsByUserAndAdvert(User user, Advert advert);
}
