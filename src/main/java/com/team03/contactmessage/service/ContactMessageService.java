package com.team03.contactmessage.service;

import com.team03.ai.ChatController;
import com.team03.contactmessage.dto.ContactMessageRequest;
import com.team03.contactmessage.dto.ContactMessageResponse;
import com.team03.contactmessage.entity.ContactMessage;
import com.team03.contactmessage.entity.Status;
import com.team03.contactmessage.mapper.ContactMessageAutoMapping;
import com.team03.contactmessage.repository.ContactMessageRepository;
import com.team03.exception.BadRequestException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.service.helper.MethodHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContactMessageService {

    private final ContactMessageRepository contactMessageRepository;
    private final ContactMessageAutoMapping contactMessageMapper;
    private final MethodHelper methodHelper;
    private final ChatController chatController;


    public ResponseMessage<ContactMessageResponse> save(ContactMessageRequest contactMessageRequest) throws IOException {

        ContactMessage contactMessage = contactMessageMapper.requestToContactMessage(contactMessageRequest);
        contactMessage.setStatus(Status.UNREAD);
        contactMessage.setCreateAt(LocalDateTime.now());
        String subject = chatController.createSubject(contactMessage.getMessage());
        contactMessage.setSubject(subject);

        ContactMessage savedData = contactMessageRepository.save(contactMessage);

        return ResponseMessage.<ContactMessageResponse>builder()
                .message(MessageUtil.getMessage("message.created"))
                .httpStatus(HttpStatus.CREATED)
                .object(contactMessageMapper.contactMessageToResponse(savedData))
                .build();
    }

    public ResponseMessage<Page<ContactMessageResponse>> getAll(String q, String requestStatus, int page, int size, String sort, String type) {
        Pageable pageable = methodHelper.getPageableWithProperties(page, size, sort, type);

        Status status = null;
        if (Status.READ.getName().equalsIgnoreCase(requestStatus)){
            status = Status.READ;
        } else if (Status.UNREAD.getName().equalsIgnoreCase(requestStatus)) {
            status = Status.UNREAD;
        } else if (!Objects.isNull(requestStatus) && !requestStatus.equalsIgnoreCase("All")){
            throw new BadRequestException(MessageUtil.getMessage("invalid.status"));
        }


        Page<ContactMessage> contactMessages = contactMessageRepository.findBySearchCriteria(q, status, pageable);

        return ResponseMessage.<Page<ContactMessageResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(contactMessages.map(contactMessageMapper::contactMessageToResponse))
                .build();
    }


    public ResponseMessage <ContactMessageResponse> getContactMessageById(Long contactMessageId) {
         ContactMessage contactMessage = contactMessageRepository.findById(contactMessageId).orElseThrow(()->
                 new ResourceNotFoundException(MessageUtil.getMessage("not.found.message"))); //NOT_FOUND_MESSAGE' dan suan ki yazim ile cagrilmali
         contactMessage.setStatus(Status.READ);
         ContactMessage contactMessage1=contactMessageRepository.save(contactMessage);
         return ResponseMessage.<ContactMessageResponse> builder()
                .httpStatus(HttpStatus.OK)
                .object(contactMessageMapper.contactMessageToResponse(contactMessage1))
                .build();
    }

    public ResponseMessage<String> deleteById(Long contactMessageId) {
        getContactMessageById(contactMessageId);
        contactMessageRepository.findById(contactMessageId).orElseThrow(()->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.message")));
        contactMessageRepository.deleteById(contactMessageId);
            return ResponseMessage.<String> builder()
                    .httpStatus(HttpStatus.OK)
                    .message(MessageUtil.getMessage("contact.message.delete"))
                    .build();
    }
}

















