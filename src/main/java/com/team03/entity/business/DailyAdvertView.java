package com.team03.entity.business;

import jakarta.persistence.*;
import lombok.*;

import java.net.InetAddress;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "daily_advert_views")
public class DailyAdvertView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Advert advert;

    @Column(nullable = false, length = 45) // Assuming IP address length is limited
    private InetAddress ipAddress;

}


