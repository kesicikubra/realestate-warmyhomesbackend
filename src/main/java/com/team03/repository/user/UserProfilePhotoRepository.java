package com.team03.repository.user;

import com.team03.entity.user.ProfilePhoto;
import com.team03.entity.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface UserProfilePhotoRepository extends JpaRepository< ProfilePhoto, Long> {
    Optional<ProfilePhoto> findByUser(User user);
}
