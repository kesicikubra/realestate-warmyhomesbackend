package com.team03.entity.user;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profile_photos")
public class ProfilePhoto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;
    private String name;
    private String type;

    @OneToOne(mappedBy = "profilePhoto")
    private User user;
}
