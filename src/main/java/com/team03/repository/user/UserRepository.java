package com.team03.repository.user;

import com.team03.entity.enums.RoleType;
import com.team03.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
    @Query(value = "SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email") String email);

    Boolean existsByEmail(String email);
    Boolean existsByPhoneNumber(String phoneNumber);

    @Query("SELECT (COUNT(u) > 0) FROM User u INNER JOIN u.userRoles r WHERE u.email = :email AND r.roleType = :roleType")
    boolean existUserByRole(String email, RoleType roleType);

    @Query(value = "SELECT COUNT(u) FROM User u JOIN u.userRoles r WHERE r.roleType = ?1")
    long countAdmin(RoleType roleType);
    @Query(value = "SELECT COUNT(u) FROM User u JOIN u.userRoles r WHERE r.roleType = ?1")
    long countCustomer(RoleType roleType);
    @Query("SELECT u FROM User u WHERE " +
            "COALESCE(:q, '') = '' OR " +
            "(LOWER(u.firstName) LIKE LOWER(concat('%', :q, '%'))) OR " +
            "(LOWER(u.lastName) LIKE LOWER(concat('%', :q, '%'))) OR " +
            "(LOWER(u.email) LIKE LOWER(concat('%', :q, '%'))) OR " +
            "(LOWER(u.phoneNumber) LIKE LOWER(concat('%', :q, '%')))")
    Page<User> findBySearchQuery(@Param("q") String q, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.resetPasswordCode = :resetPasswordCode")
    Optional <User> findByResetPasswordCode(@Param("resetPasswordCode") String resetPasswordCode);

    @Query("SELECT u FROM User u JOIN u.userRoles ur WHERE ur.name = :role")
    List<User> findAllByRole(@Param("role") String role);

    User findByPhoneNumber(String phoneNumber);

    @Query("SELECT u FROM User u WHERE u.profilePhoto.id = :photoId")
    Optional<User> findPhotoById(@Param("photoId") Long photoId);

    @Query("SELECT u FROM User u WHERE u.builtIn = ?1")
    List<User> findAllByBuiltIn(boolean b);
}