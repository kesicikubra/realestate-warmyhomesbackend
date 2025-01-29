package com.team03.contactmessage.controller;

import com.team03.contactmessage.dto.ContactMessageRequest;
import com.team03.contactmessage.dto.ContactMessageResponse;
import com.team03.contactmessage.service.ContactMessageService;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.security.annotation.IsRoleAdminAndManager;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/contact-messages")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    @PostMapping("/save")
    @Operation(tags = "Contact Message", summary = "J01")
    public ResponseMessage<ContactMessageResponse> save(@RequestBody @Valid ContactMessageRequest contactMessageRequest) throws IOException {
        return contactMessageService.save(contactMessageRequest);
    }

    @GetMapping
    @IsRoleAdminAndManager
    @Operation(tags = "Contact Message", summary = "J02", description = "ADMIN MANAGER")
    public ResponseMessage<Page<ContactMessageResponse>> getAll(
            @RequestParam(value = "q", required = false) String q,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type

    ) {
        return contactMessageService.getAll(q, status, page, size, sort, type);
    }

    @IsRoleAdminAndManager
    @GetMapping("/{id}")
    @Operation(tags = "Contact Message", summary = "J03", description = "ADMIN MANAGER")
    public ResponseMessage<ContactMessageResponse> getByIdParam(@PathVariable(name = "id") Long contactMessageId) {
        return contactMessageService.getContactMessageById(contactMessageId);
    }

    @IsRoleAdminAndManager
    @DeleteMapping("/{id}")
    @Operation(tags = "Contact Message", summary = "J04", description = "ADMIN MANAGER")
    public ResponseMessage<String> deleteById(@PathVariable(name = "id") Long contactMessageId) {
        return contactMessageService.deleteById(contactMessageId);
    }

}






























