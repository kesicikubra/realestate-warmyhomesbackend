package com.team03.service.helper;

import com.team03.entity.business.Advert;
import com.team03.entity.business.AdvertType;
import com.team03.entity.business.Category;
import com.team03.entity.user.User;
import com.team03.exception.BadRequestException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.AdvertRepository;
import com.team03.repository.business.AdvertsTypeRepository;
import com.team03.repository.business.CategoryRepository;
import com.team03.repository.business.TourRequestRepository;
import com.team03.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MethodHelper {

    private final UserRepository userRepository;
    private final AdvertRepository advertRepository;
    private final CategoryRepository categoryRepository;
    private final TourRequestRepository tourRequestRepository;
    private final AdvertsTypeRepository advertsTypeRepository;

    public User getUserByHttpServletRequest(HttpServletRequest httpServletRequest) {

        String email = (String) httpServletRequest.getAttribute("email");
        return userRepository.findByEmail(email);
//                .orElseThrow(()->
//                new ResourceNotFoundException(MessageUtil.getMessage("not.found.user.message")));
    }

    public Pageable getPageableWithProperties(int page, int size, String sort, String type) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        if (Objects.equals(type, "desc")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }

        return pageable;
    }

    public Advert getAdvertById(Long advertId) {
        return advertRepository.findById(advertId).orElseThrow(() ->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.by.id", advertId)));
    }

    public ResponseMessage<String> buildResponseMessage(String message, HttpStatus status) {
        return ResponseMessage.<String>builder()
                .message(message)
                .httpStatus(status)
                .build();
    }



    public Long getCountAdvert() {
        return advertRepository.count();

    }

    public Long getCountUser() {
        return userRepository.count();

    }

    public Long getCountAdvertType() {
        return advertsTypeRepository.count();

    }

    public Long getCategoryCount() {
        return categoryRepository.count();

    }

    public Long getCountTourRequest() {
        return tourRequestRepository.count();

    }


    public User getUserById(Long id) {
      return userRepository.findById(id).orElseThrow(()->
              new ResourceNotFoundException(MessageUtil.getMessage("not.found.user", id)));
    }

    public void isImageEmpty(MultipartFile image) {
        if(image == null || image.isEmpty() || Objects.requireNonNull(image.getOriginalFilename()).isEmpty()){
            throw new BadRequestException(MessageUtil.getMessage("not.found.image"));
        }
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException(MessageUtil.getMessage("not.found.by.id",categoryId)));
    }

    public AdvertType getAdvertTypeById(Long id) {
        return advertsTypeRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.type", id)));
    }
}

