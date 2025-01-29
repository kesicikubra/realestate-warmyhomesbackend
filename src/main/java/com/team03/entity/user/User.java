package com.team03.entity.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Builder(toBuilder = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Size(min = 2,max = 30,message = "{user.firstname.size}")
    @Column(nullable = false,length = 30)
    private String firstName;


    @Size(min = 2,max = 30,message = "{user.lastname.size}")
    @Column(nullable = false,length = 30)
    private String lastName;

    @Column(unique = true, nullable = false)
    //@Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
    private String phoneNumber;

    @Column(unique = true, nullable = false,length = 80)
    @Size(min = 10,max = 80,message = "{user.email.size}")
    @Email
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Size(min = 8, max = 60,message = "{user.password.size}")
    //@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\\\d)(?=.*[-+_!@#$%^&*., ?]).+$")
    private String password;

    private String resetPasswordCode;

    @Column(nullable = false)
    private Boolean builtIn;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime createAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime updateAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set <UserRole> userRoles;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private ProfilePhoto profilePhoto;

    @Column(name = "enabled")
    private boolean enabled;



}
