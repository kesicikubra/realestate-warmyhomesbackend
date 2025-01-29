package com.team03.repository.user;


import com.team03.entity.enums.RoleType;
import com.team03.entity.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    @Query("SELECT u FROM UserRole u WHERE u.roleType= ?1")
    Optional<UserRole> findByEnumRoleEquals(RoleType roleType);

}
