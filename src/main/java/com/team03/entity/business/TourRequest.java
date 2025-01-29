package com.team03.entity.business;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.team03.entity.listener.TourRequestEntityListener;
import com.team03.entity.enums.TourReqStatus;
import com.team03.entity.user.User;
import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "tour_request")
@EntityListeners(TourRequestEntityListener.class)
public class TourRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TourReqStatus tourReqStatus;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "US")
    private LocalDate tourDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm", timezone = "US")
    private LocalTime tourTime;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime createAt;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "ownerUserId")
    private User ownerUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "guestUserId")
    private User guestUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name = "advertId")
    private Advert advert;


}