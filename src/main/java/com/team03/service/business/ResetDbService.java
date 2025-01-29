package com.team03.service.business;

import com.team03.entity.business.*;
import com.team03.entity.user.User;
import com.team03.exception.BadRequestException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.*;
import com.team03.repository.user.RefreshTokenRepository;
import com.team03.repository.user.UserProfilePhotoRepository;
import com.team03.repository.user.UserRepository;
import com.team03.repository.user.VerificationTokenRepository;
import com.team03.service.user.AuthenticationService;
import com.team03.service.user.RefreshTokenService;
import com.team03.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResetDbService {


    private final AdvertRepository advertRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final AdvertService advertService;
    private final AdvertsTypeRepository advertsTypeRepository;
    private final TourRequestRepository tourRequestRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserProfilePhotoRepository userProfilePhotoRepository;

    public ResponseMessage< String> resetDatabase(){


        List< Advert > adverts = advertRepository.findAllByBuiltIn(false);
        deleteAdverts (adverts);
        advertRepository.deleteAll(adverts);


        List< AdvertType >advertTypes=advertsTypeRepository.findAllByBuiltIn(false);
        advertsTypeRepository.deleteAll(advertTypes);


        List< Category > categories = categoryRepository.findAllByBuiltIn(false);
        categoryRepository.deleteAll(categories);


        userProfilePhotoRepository.deleteAll ();
        verificationTokenRepository.deleteAll ();
        tourRequestRepository.deleteAll ();


        List< User > users = userRepository.findAllByBuiltIn(false);

        authenticationService.deleteUser(users);
        userRepository.deleteAll(users);

        return ResponseMessage.<String>builder ()
                .message ("The database has successfully been reset.")
                .build ();

    }

    public void deleteAdverts(List<Advert> adverts){

        for (Advert advert: adverts){

            if (Boolean.TRUE.equals(advert.getBuiltIn())) {
                throw new BadRequestException(MessageUtil.getMessage ("not.permitted.method.message"));
            }
            advertService.deleteAdvert (advert.getId ());
        }
    }

}

