package com.team03.service.business;

import com.team03.entity.business.*;
import com.team03.entity.enums.AdvertStatus;
import com.team03.entity.user.User;
import com.team03.exception.BadRequestException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.AdvertMapper;
import com.team03.payload.mappers.CategoryPropertyValueMapper;
import com.team03.payload.request.abstracts.BaseAdvertRequest;
import com.team03.payload.request.business.*;
import com.team03.payload.response.business.*;
import com.team03.repository.business.AdvertRepository;
import com.team03.repository.business.DailyAdvertViewRepository;
import com.team03.service.email.EmailService;
import com.team03.service.helper.MethodHelper;
import com.team03.service.helper.RestPage;
import com.team03.service.helper.SlugHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdvertService {

    private final AdvertRepository advertRepository;
    private final AdvertMapper advertMapper;
    private final ImageService imageService;
    private final MethodHelper methodHelper;
    private final SlugHelper slugHelper;
    private final CategoryPropertyValueService categoryPropertyValue;
    private final AdvertTypeService advertTypeService;
    private final CountryService countryService;
    private final DistrictService districtService;
    private final CityService cityService;
    private final CategoryService categoryService;
    private final DailyAdvertViewRepository dailyAdvertViewRepository;

    private final CategoryPropertyValueMapper valueMapper;
    private final FavoriteService favoriteService;
    private final EmailService emailService;


    public ResponseMessage<AdvertWithPropertiesResponse> saveAdvert(AdvertRequest advertRequest, HttpServletRequest request) {

        Advert advert = advertMapper.advertRequestToAdvert(advertRequest);
        User user = methodHelper.getUserByHttpServletRequest(request);
        checkCategoryAndKey(advertRequest.getProperties(),advertRequest.getCategory_id());
        long featuredCount = advertRequest.getImages().stream()
                .peek(imageRequest -> methodHelper.isImageEmpty(imageRequest.getImage()))
                .filter(ImageRequest::getFeatured)
                .count();

        if (featuredCount != 1) {
            throw new BadRequestException(MessageUtil.getMessage("image.should.be.one.featured"));
        }
        Long userId = user.getId();
        String slug= slugHelper.createSlug(advertRequest.getTitle()) + "-" + userId;
        if (advertRepository.existsSlugByUserId(userId, slug)){
            String title = advertRequest.getTitle();
            throw new BadRequestException(MessageUtil.getMessage("already.exists.title.for.slug", title));
        }
        advert.setSlug(slug);
        advert.setUser(user);
        advert.setStatus(AdvertStatus.PENDING);
        advert.setIsActive(true);
        advert.setBuiltIn(false);
        advert.setViewCount(0);
        Advert savedAdvert = helperAdvertMapper(advert, advertRequest);
        imageService.uploadImage(advertRequest.getImages(), savedAdvert);
        List<PropertyResponse> properties = saveProperties(advertRequest.getProperties(), savedAdvert);

        return  ResponseMessage.<AdvertWithPropertiesResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .message("advert.created.message")
                .object(advertMapper.advertToAdvertResponseWithProperties(savedAdvert, properties))
                .build();
    }

    public ResponseMessage<List<CityAmountResponse>> getCitiesAmountByAdvert() {
        List<Object[]> results = advertRepository.findCityAndAdvertCount();

        if (results != null && !results.isEmpty()) {
            List<CityAmountResponse> responses = new ArrayList<>();

            for (Object[] result : results) {
                Long cityId = (Long) result[1];
                String cityName = (String) result[0];
                Long amount = (Long) result[2];
                responses.add(new CityAmountResponse(cityId,cityName, amount));
            }

            return ResponseMessage.<List<CityAmountResponse>>builder()
                    .httpStatus(HttpStatus.OK)
                    .object(responses)
                    .build();
        }

        return ResponseMessage.<List<CityAmountResponse>>builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(MessageUtil.getMessage("advert.count.by.city"))
                .build();
    }

    public ResponseMessage<List<CategoryAmountResponse>> getCategoriesAmountByAdvert() {
        List<Object[]> results = advertRepository.findAdvertsCountByCategory();

        if (results != null && !results.isEmpty()) {
            List<CategoryAmountResponse> responses = new ArrayList<>();

            for (Object[] result : results) {
                Category category = (Category) result[0];
                String icon = (String) result[1];
                String title = (String) result[2];
                Long amount = (Long) result[3];

                CategoryAmountResponse response = CategoryAmountResponse.builder()
                        .category_id(category.getId())
                        .icon(icon)
                        .category_title(Objects.equals(category.getBuiltIn(), true) ?
                                MessageUtil.getMessage(title) : title)
                        .amount(amount)
                        .build();
                responses.add(response);
            }

            return ResponseMessage.<List<CategoryAmountResponse>>builder()
                    .httpStatus(HttpStatus.OK)
                    .object(responses)
                    .build();
        }

        return ResponseMessage.<List<CategoryAmountResponse>>builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .message(MessageUtil.getMessage("advert.count.by.category"))
                .build();
    }

    //  @Cacheable(cacheNames = "advers")
    public ResponseMessage<Page<AdvertResponseWithImageAndTourReq>> getAuthenticatedUserAdverts(
            HttpServletRequest request, int page, int size, String sort, String type) {

        Pageable pageable = methodHelper.getPageableWithProperties(page,size,sort,type);
        Page<Advert> adverts = advertRepository.findAuthenticatedUserAdverts((String) request.getAttribute("email"), pageable);

        RestPage<AdvertResponseWithImageAndTourReq>response=new RestPage<> (adverts.map(advertMapper::advertToAdvertResponseWithImageAndTourReq));

        if (adverts.isEmpty()){
            throw new BadRequestException(MessageUtil.getMessage("not.found.advert"));
        }
        return ResponseMessage.<Page<AdvertResponseWithImageAndTourReq>>builder()
                .httpStatus(HttpStatus.OK)
                .object(response)
                .build();

    }
    //  @Cacheable(value = "adverts",key= "#id")
    public ResponseMessage<AdvertResponseWithTourRequest> getAuthenticatedUserAdvertById(HttpServletRequest request, Long id) {

        User user = methodHelper.getUserByHttpServletRequest(request);
        Optional<Advert> optionalAdvert = advertRepository.findByIdAndUser(id, user);
        if (optionalAdvert.isPresent()){
            Advert advert = optionalAdvert.get();
            List<PropertyResponse> properties = getPropertiesByAdvert(advert);
            return ResponseMessage.<AdvertResponseWithTourRequest>builder()
                    .httpStatus(HttpStatus.OK)
                    .object(advertMapper.advertToAdvertResponseWithTourRequest(advert, properties))
                    .build();
        }
        throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.by.id", id));
    }

    //  @Cacheable(value = "adverts",key = "#slug")
    public ResponseMessage<AdvertResponseWithTourRequest> getAdvertBySlug(String slug,HttpServletRequest request) {

        Optional<Advert> optionalAdvert = advertRepository.findBySlug(slug);

        if (optionalAdvert.isEmpty()) {
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.by.slug", slug));
        }

        Advert advert = optionalAdvert.get();
        String forwardedIp = request.getHeader("X-Forwarded-For");
        String localIp = request.getLocalAddr();
        String userIp = (forwardedIp != null ? forwardedIp.split(",", 2)[0] : localIp);

        InetAddress userIpAddress;
        try {
            userIpAddress = InetAddress.getByName(userIp);
        } catch (UnknownHostException e) {
            throw new BadRequestException(e.getMessage());
        }

        boolean alreadyViewed = advert.getViews().stream().anyMatch(view -> view.getIpAddress().equals(userIpAddress));
        if (!alreadyViewed) {
            DailyAdvertView view = DailyAdvertView.builder().ipAddress(userIpAddress).advert(advert).build();
            dailyAdvertViewRepository.save(view);
            advert.setViewCount(advert.getViewCount() + 1);
            advertRepository.save(advert);
        }
        List<PropertyResponse> properties = getPropertiesByAdvert(advert);

        return ResponseMessage.<AdvertResponseWithTourRequest>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("advert.found.by.slug"))
                .object(advertMapper.advertToAdvertResponseWithTourRequest(advert, properties))
                .build();

    }

    //    @Cacheable(value = "advert",
//            key = "'query:' + #query + ',' +" +
//                    "'categoryId:' + #categoryId + ',' +" +
//                    "'advertTypeId:' + #advertTypeId + ',' +" +
//                    "'priceStart:' + #priceStart + ',' +" +
//                    "'priceEnd:' + #priceEnd")
    public ResponseMessage<RestPage<AdvertResponseWithFeaturedImg>> searchAdverts(
            String query, Long categoryId, Long advertTypeId, Double priceStart, Double priceEnd, int page, int size, String sort, String type, Long cityId, HttpServletRequest request) {

        priceControl(priceEnd, priceStart);
        Pageable pageable = methodHelper.getPageableWithProperties(page,size,sort,type);
        User user = methodHelper.getUserByHttpServletRequest(request);
        Page<Advert> adverts= advertRepository.findAdvertsByCriteria(query, categoryId, advertTypeId, priceStart, priceEnd, cityId, pageable);


        Page<AdvertResponseWithFeaturedImg> favoritedAdverts = adverts.map(advert -> {
            boolean isFavorited = false;
            if (!Objects.isNull(user)) {
                isFavorited = favoriteService.existsFavoriteByUserAndAdvert(user, advert);
            }
            return advertMapper.advertToAdvertResponseWithFeaturedImg(advert, isFavorited);
        });

        return ResponseMessage.<RestPage<AdvertResponseWithFeaturedImg>>builder()
                .httpStatus(HttpStatus.OK)
                .object(new RestPage<> (favoritedAdverts))
                .build();

    }

    public ResponseMessage<Page<AdvertResponseWithImageAndTourReq>> searchAdvertsByAdminAndManager(
            String query, Long categoryId, Long advertTypeId, Integer status, int page, int size, String sort, String type) {

        if (query == null && categoryId == null && advertTypeId == null && status == null) {
            throw new BadRequestException(MessageUtil.getMessage("all.parameters.null"));
        }

        AdvertStatus advertStatus = null;
        if (!Objects.isNull(status)){
            advertStatus = getAdvertStatus(status);
        }

        Pageable pageable = methodHelper.getPageableWithProperties(page,size,sort,type);
        Page<Advert> adverts= advertRepository.findAdvertsByCriteriaWithStatus(query, categoryId, advertTypeId, advertStatus, pageable);

        return ResponseMessage.<Page<AdvertResponseWithImageAndTourReq>>builder()
                .httpStatus(HttpStatus.OK)
                .object(new RestPage<> (adverts.map(advertMapper::advertToAdvertResponseWithImageAndTourReq)))
                .build();
    }

    // @Cacheable(value = "adverts",key = "#id")
    public ResponseMessage<AdvertResponseWithBuiltIn> getAdvertById(Long id) {
        Optional<Advert> advert = advertRepository.findById(id);

        if(advert.isEmpty()){
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.by.id", id));
        }
        List<PropertyResponse> properties = getPropertiesByAdvert(advert.get());

        return ResponseMessage.<AdvertResponseWithBuiltIn>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("advert.found.by.id", id))
                .object(advertMapper.advertToAdvertResponseWithBuiltIn(advert.get(), properties))
                .build();
    }

    // @CachePut(value = "adverts" , key = "#id")
    public ResponseMessage<AdvertResponseWithoutImage> updateAdvertByCustomer(HttpServletRequest request, Long id, AdvertRequestWithoutImages advertRequest) {

        User user = methodHelper.getUserByHttpServletRequest(request);
        Long userId = user.getId();
        Optional<Advert> savedAdvert = advertRepository.findByIdAndUser(id,user);

        if (savedAdvert.isEmpty()){
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.by.id", id));
        }

        if (savedAdvert.get().getBuiltIn()){
            throw new BadRequestException(MessageUtil.getMessage("built.in.advert"));
        }
        checkCategoryAndKey(advertRequest.getProperties(),advertRequest.getCategory_id());
        Advert advert = advertMapper.advertRequestToAdvertForUpdate(advertRequest, savedAdvert.get());
        String slug= slugHelper.createSlug(advertRequest.getTitle()) + "-" + userId;
        if (advertRepository.existsSlugByUserId(userId, slug) && !Objects.equals(advertRequest.getTitle(), savedAdvert.get().getTitle())){
            throw new BadRequestException(MessageUtil.getMessage("already.exists.title.for.slug", advertRequest.getTitle()));
        }
        advert.setSlug(slug);
        advert.setUser(user);
        advert.setStatus(AdvertStatus.PENDING);
        advert.setIsActive(advertRequest.is_active());
        Advert updatedAdvert = helperAdvertMapper(advert, advertRequest);

        List<PropertyResponse> responseProperties = updateProperties(advertRequest.getProperties(), updatedAdvert);

        return  ResponseMessage.<AdvertResponseWithoutImage>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("advert.updated.message"))
                .object(advertMapper.advertToAdvertResponseWithoutImage(updatedAdvert, responseProperties))
                .build();
    }

    //   @CachePut(value = "adverts", key = "#id")
    public ResponseMessage<AdvertResponseWithoutImage> updateAdvert(Long id, AdvertRequestUpdateAdmin advertRequest) {

        Optional<Advert> savedAdvertOpt = advertRepository.findById(id);
        if (savedAdvertOpt.isEmpty()){
            throw new BadRequestException(MessageUtil.getMessage("not.found.advert.by.id",id));
        }

        Advert savedAdvert = savedAdvertOpt.get();
        if (savedAdvert.getBuiltIn()){
            throw new BadRequestException(MessageUtil.getMessage("built.in.advert",id));
        }

        Integer dbStatus = savedAdvert.getStatus().getId();
        checkCategoryAndKey(advertRequest.getProperties(),advertRequest.getCategory_id());
        Advert advert = advertMapper.advertRequestToAdvertForUpdate(advertRequest, savedAdvert);
        advert.setIsActive(true);

        Integer status = advertRequest.getStatus();
        AdvertStatus advertStatus = getAdvertStatus(status);

        // rejected olmadigi durumlarda true setlenecek
        advert.setIsActive(!Objects.equals(advertStatus, AdvertStatus.REJECTED));
        advert.setStatus(advertStatus);
        User user = savedAdvert.getUser();
        advert.setUser(user);
        String slug= slugHelper.createSlug(advertRequest.getTitle()) + "-" + user.getId();
        if (advertRepository.existsSlugByUserId(user.getId(), slug) && !Objects.equals(advertRequest.getTitle(), savedAdvert.getTitle())){
            throw new BadRequestException(MessageUtil.getMessage("already.exists.title.for.slug", advertRequest.getTitle()));
        }
        advert.setSlug(slug);
        Advert updatedAdvert = helperAdvertMapper(advert, advertRequest);
        if (updatedAdvert.getStatus().equals(AdvertStatus.ACTIVATED)){
            imageService.updateImageSuitable(updatedAdvert.getId());
        }

        List<PropertyResponse> responseProperties = updateProperties(advertRequest.getProperties(), updatedAdvert);

        if (!Objects.equals(status, dbStatus) && (status.equals(1) || status.equals(2))){
            emailService.sendActivatedOrRejectedAdvertEmail(user.getEmail(), user.getFirstName(), advertRequest.getStatus());
        }

        return  ResponseMessage.<AdvertResponseWithoutImage>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("advert.updated.by.admin.message"))
                .object(advertMapper.advertToAdvertResponseWithoutImage(updatedAdvert, responseProperties))
                .build();
    }

    //  @CacheEvict(value = "adverts",key = "#id")
    public ResponseMessage<AdvertResponseWithoutImage> deleteAdvert(Long id) {

        Optional<Advert> advert = advertRepository.findById(id);
        if (advert.isEmpty()){
            throw new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.by.id", id));
        }
        if (advert.get().getBuiltIn()){
            throw new BadRequestException(MessageUtil.getMessage("built.in.advert"));
        }

        List<CategoryPropertyValue> values = advert.get().getCategoryPropertyValues();
        List<PropertyResponse> properties = new ArrayList<>();

        for (CategoryPropertyValue value:values) {
            PropertyResponse property = valueMapper.categoryPropertyValueToPropertyResponse(value.getCategoryPropertyKey(), value);
            properties.add(property);
        }

        advertRepository.deleteById(id);
        return  ResponseMessage.<AdvertResponseWithoutImage>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("advert.deleted.message"))
                .object(advertMapper.advertToAdvertResponseWithoutImage(advert.get(), properties))
                .build();
    }



    //  @Cacheable(value = "adverts",key = "#amount")
    public ResponseMessage<List<AdvertResponseWithFeaturedImg>> getPopularAdverts(int amount, HttpServletRequest request) {

        Pageable pageable = PageRequest.of(0, amount); // 0'dan başlayarak belirli miktar kadar sonucu getir
        List<Advert> mostPopularAdverts = advertRepository.findMostPopularAdverts(pageable);

        User user = methodHelper.getUserByHttpServletRequest(request);
        List<AdvertResponseWithFeaturedImg> favoritedAdverts = mostPopularAdverts.stream().map(advert -> {
            boolean isFavorited = false;
            if (!Objects.isNull(user)) {
                isFavorited = favoriteService.existsFavoriteByUserAndAdvert(user, advert);
            }
            return advertMapper.advertToAdvertResponseWithFeaturedImg(advert, isFavorited);
        }).toList();

        return   ResponseMessage.<List<AdvertResponseWithFeaturedImg>>builder()
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("advert.most.popular"))
                .object(favoritedAdverts)
                .build();
    }

    private Advert helperAdvertMapper(Advert advert, BaseAdvertRequest advertRequest) {

        advert.setAdvertType(advertTypeService.getAdvertTypeById(advertRequest.getAdvert_type_id()));
        advert.setCountry(countryService.getCountryByCityId(advertRequest.getCountry_id(),advertRequest.getCity_id()));
        advert.setCity(cityService.getCountryByCityId(advertRequest.getCity_id(),advertRequest.getDistrict_id()));
        advert.setDistrict(districtService.getDistrictById(advertRequest.getDistrict_id()));
        advert.setCategory(categoryService.getCategoryById(advertRequest.getCategory_id()));

        return advertRepository.save(advert);
    }

    private List<PropertyResponse> saveProperties(List<PropertyRequest> properties, Advert advert) {
        List<PropertyResponse> responseProperties = new ArrayList<>();

        for (PropertyRequest property:properties) {
            PropertyResponse responseProperty = categoryPropertyValue.saveCategoryPropertyValue(property, advert);
            if (responseProperty != null) {
                responseProperties.add(responseProperty);
            }
        }
        return responseProperties;
    }

    private List<PropertyResponse> updateProperties (List<PropertyRequest> properties, Advert advert) {
        List<PropertyResponse> responseProperties = new ArrayList<>();

        for (PropertyRequest property:properties) {
            PropertyResponse responseProperty = categoryPropertyValue.updateCategoryPropertyValue(property, advert);
            if (responseProperty != null) {
                responseProperties.add(responseProperty);
            }
        }
        return responseProperties;
    }

    private void checkCategoryAndKey(List<PropertyRequest> properties, Long categoryId) {
        List<Long> keyIds = properties.stream()
                .map(PropertyRequest::getKeyId).toList();
        categoryService.checkCategoryKeyById(categoryId, keyIds);
        if (keyIds.stream().distinct().count() != keyIds.size()) {
            throw new BadRequestException(MessageUtil.getMessage("duplicate.key.id"));
        }
    }

    private void priceControl(Double priceEnd, Double priceStart) {
        if ((!Objects.isNull(priceStart) && priceStart < 0) || (!Objects.isNull(priceEnd) && priceEnd < 0)) {
            throw new BadRequestException(MessageUtil.getMessage("negative.price"));
        }
        if (!Objects.isNull(priceStart) && priceStart >= 0 && !Objects.isNull(priceEnd) && priceEnd >= 0 && priceEnd < priceStart) {
            throw new BadRequestException(MessageUtil.getMessage("wrong.price"));
        }
    }

    public List<Advert> findAdvertsBetweenDatesAndFilters(LocalDate beginDate, LocalDate endDate, Long categoryTitleId,
                                                          Long typeTitleId, Integer status) {

        String typeTitle= methodHelper.getAdvertTypeById(typeTitleId).getTitle();
        String categoryTitle=methodHelper.getCategoryById(categoryTitleId).getTitle();

        AdvertStatus advertStatus = null;
        if(!Objects.isNull(status)){
            advertStatus = getAdvertStatus(status);
        }

        return advertRepository.findAdvertsBetweenDatesAndFilters(beginDate,endDate,categoryTitle, typeTitle,advertStatus);
    }

    public AdvertStatus getAdvertStatus(Integer status) {
        AdvertStatus advertStatus;
        if(AdvertStatus.ACTIVATED.getId().equals(status)){
            advertStatus = AdvertStatus.ACTIVATED;
        } else if (AdvertStatus.REJECTED.getId().equals(status)) {
            advertStatus = AdvertStatus.REJECTED;
        } else if (AdvertStatus.PENDING.getId().equals(status)) {
            advertStatus = AdvertStatus.PENDING;
        } else {
            throw new BadRequestException(MessageUtil.getMessage("invalid.advert.status"));
        }
        return advertStatus;
    }

    public List<Advert> findAdvertsWithMostTourRequests(Integer numberOfAdverts) {
        return advertRepository.findAdvertsWithMostTourRequests(numberOfAdverts);

    }
    private List<PropertyResponse> getPropertiesByAdvert(Advert advert) {

        List<CategoryPropertyValue> values = advert.getCategoryPropertyValues();
        List<PropertyResponse> properties = new ArrayList<>();

        for (CategoryPropertyValue value:values) {
            PropertyResponse property = valueMapper.categoryPropertyValueToPropertyResponse(value.getCategoryPropertyKey(), value);
            properties.add(property);
        }
        return properties;
    }

    public RestPage<AdvertResponseForGetUser> getAdvertsByUserId(Long id, int page, int size, String sort, String type) {
        Pageable pageable = methodHelper.getPageableWithProperties(page, size, sort, type);

        return new RestPage<> (advertRepository.findByUserId(id, pageable)
                .map(this::convertToAdvertResponse));
    }

    private AdvertResponseForGetUser convertToAdvertResponse(Advert advert) {
        AdvertResponseForGetUser advertResponse = advertMapper.advertToAdvertResponseForGetUser (advert);
        List<PropertyResponse> properties = getPropertiesByAdvert(advert);
        advertResponse.setProperties (properties);
        return advertResponse;
    }

}
