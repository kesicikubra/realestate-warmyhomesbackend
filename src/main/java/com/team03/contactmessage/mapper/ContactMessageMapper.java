
//package com.team03.contactmessage.mapper;
//
//import com.team03.contactmessage.dto.ContactMessageRequest;
//import com.team03.contactmessage.dto.ContactMessageResponse;
//import com.team03.contactmessage.entity.ContactMessage;
//import com.team03.contactmessage.entity.Status;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class ContactMessageMapper {
//
//    public ContactMessage requestToContactMessage(ContactMessageRequest contactMessageRequest) {
//
//        return ContactMessage.builder()
//                .firstName(contactMessageRequest.getFirst_name())
//                .lastName(contactMessageRequest.getLast_name())
//                .email(contactMessageRequest.getEmail())
//                .message(contactMessageRequest.getMessage())
//                .createAt(LocalDateTime.now())
//                .status(Status.UNREAD)
//                .build();
//    }
//
//    public ContactMessageResponse contactMessageToResponse(ContactMessage contactMessage){
//        return ContactMessageResponse.builder()
//                .first_name(contactMessage.getFirstName())
//                .last_name(contactMessage.getLastName())
//                .email(contactMessage.getEmail())
//                .message(contactMessage.getMessage())
//                .create_at(contactMessage.getCreateAt())
//                .status(contactMessage.getStatus())
//                .subject(contactMessage.getSubject())
//                .build();
//    }
//
//
//}

