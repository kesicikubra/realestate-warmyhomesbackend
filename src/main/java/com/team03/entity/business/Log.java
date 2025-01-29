package com.team03.entity.business;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.team03.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "logs")
public class Log {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String log;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    private Advert  advert;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "US")
    private LocalDateTime createAt;


}
