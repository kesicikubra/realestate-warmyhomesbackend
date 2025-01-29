package com.team03.repository.user;

import com.team03.entity.user.User;
import com.team03.entity.user.VerificationToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken,Long> {
    VerificationToken findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationToken v WHERE v.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    @Transactional
    void deleteByExpiryDateBefore(LocalDateTime now);
}
