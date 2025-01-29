package com.team03.service.business;

import com.team03.entity.business.Advert;
import com.team03.entity.business.Favorite;
import com.team03.entity.user.User;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.AdvertMapper;
import com.team03.payload.mappers.FavoriteMapper;
import com.team03.payload.response.business.AdvertResponse;
import com.team03.payload.response.business.FavoriteResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.FavoriteRepository;
import com.team03.service.helper.MethodHelper;

import com.team03.service.helper.RestPage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AdvertMapper advertMapper;
    private final MethodHelper helper;
    private final FavoriteMapper favoriteMapper;


    public ResponseMessage< RestPage< FavoriteResponse > > getUserFavorites(Long id, int page, int size, String sort, String type) {
        return ResponseMessage.<RestPage<FavoriteResponse>>builder()
                .object(getUserFavoritesByUserId (id, page, size, sort, type))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public RestPage<FavoriteResponse> getUserFavoritesByUserId(Long id, int page, int size, String sort, String type) {
        User user = helper.getUserById(id);
        Pageable pageable = helper.getPageableWithProperties(page, size, sort, type);
       return new RestPage<> (favoriteRepository.findByUserId(user.getId(), pageable)
                .map(favoriteMapper::toFavoriteResponse));

    }

    public ResponseMessage< Page<FavoriteResponse> > getAllFavorites(HttpServletRequest request, int page, int size, String sort, String type) {

        User user = helper.getUserByHttpServletRequest(request);

        Pageable pageable = helper.getPageableWithProperties (page, size, sort, type);
        Page<FavoriteResponse> userFavorites = favoriteRepository.findByUserId(user.getId(),pageable)
                .map (favoriteMapper::toFavoriteResponse);


        return ResponseMessage.<Page<FavoriteResponse>>builder()
                .object(new RestPage<> (userFavorites))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<AdvertResponse> addOrRemoveFavorite(Long advertId, HttpServletRequest request) {

        User user = helper.getUserByHttpServletRequest(request);
        Advert advert = helper.getAdvertById(advertId);

        Optional<Favorite> favoriteOpt = favoriteRepository.findByAdvertIdAndUserId(advertId, user.getId());
        if (favoriteOpt.isPresent()) {
            favoriteRepository.deleteById(favoriteOpt.get().getId());
            return ResponseMessage.<AdvertResponse>builder()
                    .message(MessageUtil.getMessage ("favorites.deleted.message"))
                    .httpStatus(HttpStatus.OK)
                    .build();
        }
        Favorite favorite = Favorite.builder().advert(advert).user(user).createAt(LocalDateTime.now()).build();
        favoriteRepository.save(favorite);

        return ResponseMessage.<AdvertResponse>builder()
                .object(advertMapper.advertToAdvertResponse(advert))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    public ResponseMessage<String> deleteAllFavorite(HttpServletRequest request) {

        User user = helper.getUserByHttpServletRequest(request);
        List<Favorite> favorites = favoriteRepository.findAllByUser(user);
        if (favorites.isEmpty()){
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.favorite.message"));
        }

        favoriteRepository.deleteAll(favorites);
        return helper.buildResponseMessage(MessageUtil.getMessage("favorites.deleted.message"),HttpStatus.OK);
    }

    public ResponseMessage<String> removeFavoritesByUserId(Long userId) {

        List<Favorite> userFavorites = favoriteRepository.findByUserId(userId);
        favoriteRepository.deleteAll(userFavorites);
        return helper.buildResponseMessage(MessageUtil.getMessage("favorites.deleted.message"), HttpStatus.OK);
    }

    public ResponseMessage<String> removeFavoriteById(Long favoriteId) {

        Optional<Favorite> favorite= favoriteRepository.findById(favoriteId);
        if (favorite.isEmpty()) {
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.favorite.by.id.message", favoriteId));
        }
        favoriteRepository.deleteById(favoriteId);
        return helper.buildResponseMessage(MessageUtil.getMessage("favorites.deleted.message"),HttpStatus.OK);
    }

    public boolean existsFavoriteByUserAndAdvert(User user, Advert advert) {
        return favoriteRepository.existsByUserAndAdvert(user, advert);
    }
}
