package com.team03.entity.user;


import com.team03.entity.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
@Builder(toBuilder = true)
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15, name = "role_type")
    private RoleType roleType;


    @Column(nullable = false, name = "role_name")
    private String name;


}
