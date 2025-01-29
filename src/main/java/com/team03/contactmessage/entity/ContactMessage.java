package com.team03.contactmessage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "contact_message")
public class ContactMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2,max = 30,message = "Name must be at least 2 character")
    private String firstName;

    @Size(min = 2,max = 30,message = "Lastname must be at least 2 character")
    private String lastName;

    @Email(message = "please enter valid email")
    private String email;

    @Size(min = 5, max = 300,message = "Advert title should be at least 5 character")
    private String message;

    @Enumerated(EnumType.STRING)
    private Status status=Status.UNREAD;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "US")
    private LocalDateTime createAt;

    private String subject;


}