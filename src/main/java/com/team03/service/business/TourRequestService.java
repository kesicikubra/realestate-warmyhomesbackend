package com.team03.service.business;

import com.team03.entity.business.Advert;
import com.team03.entity.business.TourRequest;
import com.team03.entity.enums.TourReqStatus;
import com.team03.entity.user.User;
import com.team03.exception.BadRequestException;
import com.team03.exception.ConflictException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.TourRequestMapper;
import com.team03.payload.request.business.TourRequestRequest;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.payload.response.business.TourRequestResponse;
import com.team03.repository.business.TourRequestRepository;
import com.team03.service.email.EmailService;
import com.team03.service.helper.MethodHelper;
import com.team03.service.helper.PageableHelper;
import com.team03.service.helper.RestPage;
import com.team03.service.validator.DateTimeValidator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TourRequestService {

    private final TourRequestRepository tourRequestRepository;
    private final MethodHelper methodHelper;
    private final DateTimeValidator dateTimeValidator;
    private final TourRequestMapper tourRequestMapper;
    private final PageableHelper pageableHelper;
    private final EmailService emailService;


    //S01-AuthenticatedUsers Tour Requests ***********- tüm statüsler mi?
    public ResponseMessage<Page<TourRequestResponse>> getAuthenticatedUsersAllTourRequest(String q, int page, int size, String sort, String type,
                                                                                          HttpServletRequest httpServletRequest) {
        Pageable pageable=  pageableHelper.getPageableWithProperties(page,size,sort,type);
        User user=  methodHelper.getUserByHttpServletRequest(httpServletRequest);
        Page<TourRequestResponse> tourRequestResponses=  tourRequestRepository.findAllByAuthenticatedPage(pageable,user,q)
                .map(tourRequestMapper::tourRequestToResponse);

        return ResponseMessage.<Page<TourRequestResponse>>builder()
                .object(new RestPage<> (tourRequestResponses))
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("tour.requests.for.auth.user"))
                .build();
    }




    //S02-GET ALL TOUR REQ FOR ADMIN AND MANAGER************ - tüm statüsler mi?
    public ResponseMessage<Page<TourRequestResponse>> getAllTourRequestForManager(String q, int page, int size, String sort, String type) {

        Pageable pageable=  pageableHelper.getPageableWithProperties(page,size,sort,type);
        Page<TourRequestResponse> tourRequestResponses=  tourRequestRepository.findAllTourReqByPageForAdminAndManager(pageable,q)
                .map(tourRequestMapper::tourRequestToResponse);


        return ResponseMessage.<Page<TourRequestResponse>>builder()
                .object(new RestPage<> (tourRequestResponses))
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("tour.requests.for.auth.user"))
                .build();

    }

    //S03-GET AUTH USERS TOUR REQUEST**********
    public ResponseMessage<TourRequestResponse> getTourRequestByAuthenticatedUser(Long id, HttpServletRequest httpServletRequest) {
        //user id check
        TourRequest tourRequest = findTourRequestById(id);
        //are user and tourreq id related check
        User user = methodHelper.getUserByHttpServletRequest(httpServletRequest);

        if (user!=tourRequest.getGuestUser() && user!=tourRequest.getOwnerUser()){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.not.found.by.id",id))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        }
        TourRequestResponse tourResponse=tourRequestMapper.tourRequestToResponse(tourRequest);

        return   ResponseMessage.<TourRequestResponse>builder()
                .object(tourResponse)
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("tour.requests.founded"))
                .build();

    }

    //S04 GET FOR ADMIN OR MANAGER***********
    public ResponseMessage<TourRequestResponse> getTourRequestForAdminandManager(Long id) {
        //Bu ID ile tourreq var mi kontrolu
        TourRequest tourRequest = findTourRequestById(id);
        //mapleme
        TourRequestResponse tourResponse=tourRequestMapper.tourRequestToResponse(tourRequest);

        return   ResponseMessage.<TourRequestResponse>builder()
                .object(tourResponse)
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("tour.requests.founded"))
                .build();

    }


    //S05-CREATE*************
    public ResponseMessage<TourRequestResponse> createTourRequest(TourRequestRequest tourRequestDto,
                                                                  HttpServletRequest httpServletRequest) {

        //bu id ile bi ilan var mi?
        Advert advert=  methodHelper.getAdvertById(tourRequestDto.getAdvertId());
        //reklam hala aktif mi?
        if (!advert.getIsActive()){
            throw new ResourceNotFoundException(MessageUtil.getMessage("advert.not active", advert.getId()));
        }

        // tarih kontrolu
        if (tourRequestDto.getTour_date().isBefore(LocalDate.now())){
            throw new BadRequestException(MessageUtil.getMessage("tourrequest.cannot.create.date.time"));
        }

        if (tourRequestDto.getTour_date().isEqual(LocalDate.now()) && tourRequestDto.getTour_time().isBefore(LocalTime.now())){
            throw new BadRequestException(MessageUtil.getMessage("tourrequest.cannot.create.date.time"));

        }

        if (!isTourTimeValid(tourRequestDto.getTour_time().getMinute())){
            throw new BadRequestException(MessageUtil.getMessage("tourrequest.cannot.create.date.time"));
        }

        // tarih cakismasi kontrolu
        List<TourRequest> existsTourRequest = getAllApprovedTourRequestByAdvert(advert);
        dateTimeValidator.checkDateTime(existsTourRequest, tourRequestDto);

        User guestUser= methodHelper.getUserByHttpServletRequest(httpServletRequest);

        if (guestUser.equals(advert.getUser())){
            throw new ConflictException(MessageUtil.getMessage("owner.user.conflict"));
        }
        //istegi yapan kullanicinin baska bi basvurusu var mi ayni tarihte
        List<TourRequest> existsTourRequestbyGuest = getAllTourRequestByGuest(guestUser);
        dateTimeValidator.checkDateTime(existsTourRequestbyGuest, tourRequestDto);
        //ev sahibinin baska bi ilan icin ayni tarihte basvurusu var mi? zaten reddedebilir.


        //ilana basvuran kisinin bu ilana baska basvurusu var mi?
        TourRequest tourRequest= tourRequestMapper.requestDtoToTourRequest(tourRequestDto, advert);

        if (tourRequestRepository.existsByGuestUserAndAdvert(guestUser, advert.getId())){
            return ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.more.than.one"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        //ilgili ilanin sahibi ve bu basvuruyu yapanin eklenmesi
        tourRequest.setGuestUser(guestUser);
        tourRequest.setOwnerUser(advert.getUser());

        //status atama
        tourRequest.setTourReqStatus(TourReqStatus.PENDING);
        //create tarihi
        tourRequest.setCreateAt(LocalDateTime.now());


        //save islemi
        TourRequest savedTour= tourRequestRepository.save(tourRequest);

        //mappleme
        TourRequestResponse tourResponse=tourRequestMapper.tourRequestToResponse(savedTour);
//------ TourRequestIcinde ki advert advertResponsa donusturum o sekilde dondurulmeli,aksi takdirde bir suru city objesi oluyor------
        return ResponseMessage.<TourRequestResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(MessageUtil.getMessage("tour.request.created"))
                .object(tourResponse)
                .build();
    }
    // S06-UPDATE**********
    public ResponseMessage<TourRequestResponse> updateTourRequest(Long id, TourRequestRequest tourRequestDto,
                                                                  HttpServletRequest httpServletRequest) {
        //update edilecek req var mi?
        TourRequest tourRequest =findTourRequestById(id);

        // tarih kontrolu
        if (tourRequestDto.getTour_date().isBefore(LocalDate.now())){
            throw new BadRequestException(MessageUtil.getMessage("tourrequest.cannot.create.date.time"));
        }

        if (tourRequestDto.getTour_date().isEqual(LocalDate.now()) && tourRequestDto.getTour_time().isBefore(LocalTime.now())){
            throw new BadRequestException(MessageUtil.getMessage("tourrequest.cannot.create.date.time"));

        }

        if (!isTourTimeValid(tourRequestDto.getTour_time().getMinute())){
            throw new BadRequestException(MessageUtil.getMessage("tourrequest.cannot.create.date.time"));
        }

        //user kim
        User user =  methodHelper.getUserByHttpServletRequest(httpServletRequest);

        //tur tarihi gecmis mi
        if(isDateExpired(tourRequest.getTourDate(), tourRequest.getTourTime())) {
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.update"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        //owner kullanici olmasi mantikli mi? sonucta request baska birine ait
        if (user!=tourRequest.getOwnerUser() || user!=tourRequest.getGuestUser()){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.update"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        }

        //reklam ne durumda, reklam active degilse update olmayacak
        Advert advert = tourRequest.getAdvert();
        if (!advert.getIsActive()){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("advert.not.active", advert.getId()))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        //- Only the tour requests whose status pending or rejected can be updated
        if (tourRequest.getTourReqStatus()!=TourReqStatus.PENDING && tourRequest.getTourReqStatus()!=TourReqStatus.DECLINED){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.update"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        //yeni tarihlerdeki catismalarin kontrolu
        List<TourRequest> existsTourRequest = getAllApprovedTourRequestByAdvert(advert);
        dateTimeValidator.checkDateTime(existsTourRequest, tourRequestDto);

        //istegi yapan kullanicinin baska bi basvurusu var mi ayni tarihte
        List<TourRequest> existsTourRequestbyGuest = getAllTourRequestByGuest(tourRequest.getGuestUser());
        dateTimeValidator.checkDateTime(existsTourRequestbyGuest, tourRequestDto);

        //update edilen verileri VE UPDATE tarihini setle ve pendinge cek

        TourRequest updatedReq = tourRequestMapper.TourReqRequestroUpdatedTourRequest(id, advert, tourRequestDto,
                tourRequest.getCreateAt(), tourRequest.getGuestUser());
        updatedReq.setUpdateAt(LocalDateTime.now());
        TourRequestResponse updatedResponse = tourRequestMapper.tourRequestToResponse(tourRequestRepository.save(updatedReq));

        return ResponseMessage.<TourRequestResponse>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(MessageUtil.getMessage("tour.request.updated"))
                .object(updatedResponse)
                .build();

    }

    // S07-CANCEL***********
    public ResponseMessage<TourRequestResponse> cancelTourRequest(Long id,
                                                                  HttpServletRequest httpServletRequest) {
        TourRequest tourRequest =  findTourRequestById(id);
        User user =  methodHelper.getUserByHttpServletRequest(httpServletRequest);

        //zaman kontrolu, tarihi gecmemisse cancel edebilir

        if (isDateExpired(tourRequest.getTourDate(),tourRequest.getTourTime())){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.canceled"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }

        // tour requesti olusturan kisi yapabilecek
        if (user!=tourRequest.getGuestUser()){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.canceled"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        }

        tourRequest.setUpdateAt(LocalDateTime.now());
        tourRequest.setTourReqStatus(TourReqStatus.CANCELED);
        TourRequest canceledrequest= tourRequestRepository.save(tourRequest);


        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestMapper.tourRequestToResponse(canceledrequest))
                .message(MessageUtil.getMessage("tour.request.canceled"))
                .httpStatus(HttpStatus.OK)
                .build();
    }

    //S08-APPROVE***********
    public ResponseMessage<TourRequestResponse> approveTourRequest(Long id,
                                                                   HttpServletRequest httpServletRequest) {
        TourRequest tourRequest =  findTourRequestById(id);
        User user =  methodHelper.getUserByHttpServletRequest(httpServletRequest);
        //tarihi gecmis olanlarda degisiklik yapilmamali
        if(isDateExpired(tourRequest.getTourDate(), tourRequest.getTourTime())) {
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.approved"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        //Owner user yapabilecek
        if (user!=tourRequest.getOwnerUser()){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.approved"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        }
        //daha önce decline edilmis veya cancel edilmis olanlar approved olabilir mi?
        //burada istenilenler icinde özellikle belirtilmemis ancak kontrol yapilabilir mi?
        if (tourRequest.getTourReqStatus()!=TourReqStatus.PENDING){
            ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.approved"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        }
        tourRequest.setTourReqStatus(TourReqStatus.APPROVED);
        tourRequest.setUpdateAt(LocalDateTime.now());
        TourRequest approvedReq =  tourRequestRepository.save(tourRequest);
        emailService.sendApprovedOrDeclinedTourRequestEmail(approvedReq);
        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestMapper.tourRequestToResponse(approvedReq))
                .message(MessageUtil.getMessage("tour.request.approved"))
                .httpStatus(HttpStatus.OK)
                .build();




    }


    //S09-DECLINE***********
    public ResponseMessage<TourRequestResponse> declineTourRequest(Long id,
                                                                   HttpServletRequest httpServletRequest) {

        TourRequest tourRequest =  findTourRequestById(id);
        User user =  methodHelper.getUserByHttpServletRequest(httpServletRequest);
        //tarihi gecmis olanlarda degisiklik yapilmamali
        if(isDateExpired(tourRequest.getTourDate(), tourRequest.getTourTime())) {
            return    ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.declined"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
        //Owner user yapabilecek
        if (user!=tourRequest.getOwnerUser()){
            return   ResponseMessage.<TourRequestResponse>builder()
                    .message(MessageUtil.getMessage("tourrequest.cannot.declined"))
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();

        }

        tourRequest.setTourReqStatus(TourReqStatus.DECLINED);
        tourRequest.setUpdateAt(LocalDateTime.now());
        TourRequest declinedReq =  tourRequestRepository.save(tourRequest);
        emailService.sendApprovedOrDeclinedTourRequestEmail(declinedReq);
        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourRequestMapper.tourRequestToResponse(declinedReq))
                .message(MessageUtil.getMessage("tour.request.declined"))
                .httpStatus(HttpStatus.OK)
                .build();


    }
    //S10-DELETE*********
    public ResponseMessage<TourRequestResponse> deleteTourRequest(Long id) {
        //böyle bi request var mi ve user taraf mi
        TourRequest tourRequest =  findTourRequestById(id);
        //burda manager ve admin her durumda silme yapabilir mi? projeden anlasilmiyor
        TourRequestResponse tourResponse = tourRequestMapper.tourRequestToResponse(tourRequest);
        tourRequestRepository.delete(tourRequest);
        return ResponseMessage.<TourRequestResponse>builder()
                .object(tourResponse)
                .message(MessageUtil.getMessage("tour.request.deleted"))
                .httpStatus(HttpStatus.OK)
                .build();



    }



    //ID ILE TOUR REQUEST GETIRME
    public TourRequest findTourRequestById(Long id){
        return tourRequestRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(MessageUtil.getMessage("tourrequest.not.found.by.id",id)));
    }

    // TOURREQUESTIN TARIHI GECMIS MI KONTROLU
    public boolean isDateExpired(LocalDate tourReqDate, LocalTime tourReqTime){

        LocalDateTime today=LocalDateTime.now();
        LocalDate todaysDate= today.toLocalDate();
        LocalTime todaysTime=  today.toLocalTime();

        return tourReqDate.isBefore(todaysDate) || (tourReqDate.isEqual(todaysDate) && tourReqTime.isBefore(todaysTime));
    }

    //BI ADVERT ICIN GEREKEN TÜM APPROVED TOUR REQUESTLER
    public List<TourRequest> getAllApprovedTourRequestByAdvert(Advert advert){
      TourReqStatus status =  TourReqStatus.APPROVED;
        return  tourRequestRepository.findAllByAdvert(advert.getId(),status);
    }

    //GUEST USERIN TOUR REQUESTLERI
    public List<TourRequest> getAllTourRequestByGuest(User user){
        return  tourRequestRepository.findAllByGuestUserAndOwnerUser(user);
    }


    public List<TourRequest> findTourRequestByBetweenDatesAndStatus(LocalDate beginDate, LocalDate endDate, Integer status) {

        TourReqStatus tourReqStatus = null;
        if(!Objects.isNull(status)){
            tourReqStatus = getTourRequestStatus(status);
        }

        if (Objects.equals(status, TourReqStatus.PENDING.getId())){
            tourReqStatus = TourReqStatus.PENDING;
        } else if (Objects.equals(status, TourReqStatus.APPROVED.getId())){
            tourReqStatus = TourReqStatus.APPROVED;
        } else if (Objects.equals(status, TourReqStatus.DECLINED.getId())){
            tourReqStatus = TourReqStatus.DECLINED;
        } else if (Objects.equals(status, TourReqStatus.CANCELED.getId())){
            tourReqStatus = TourReqStatus.CANCELED;
        }

        return tourRequestRepository.findTourRequestsBetweenDatesAndStatus(beginDate, endDate, tourReqStatus);
    }

    private TourReqStatus getTourRequestStatus(Integer status) {
        TourReqStatus tourReqStatus;
        if(TourReqStatus.PENDING.getId().equals(status)){
            tourReqStatus = TourReqStatus.PENDING;
        } else if (TourReqStatus.APPROVED.getId().equals(status)) {
            tourReqStatus = TourReqStatus.APPROVED;
        } else if (TourReqStatus.DECLINED.getId().equals(status)) {
            tourReqStatus= TourReqStatus.DECLINED;
        }else if (TourReqStatus.CANCELED.getId().equals(status)) {
        tourReqStatus= TourReqStatus.CANCELED;}
        else {
            throw new BadRequestException(MessageUtil.getMessage("invalid.advert.status"));
        }
        return tourReqStatus;

    }

    public ResponseMessage<Page<TourRequestResponse>> getAuthenticatedUsersAllGuestTourRequest(String q, int page, int size, String sort,
                                                                                               String type, HttpServletRequest httpServletRequest) {

        Pageable pageable=  pageableHelper.getPageableWithProperties(page,size,sort,type);
        User user=  methodHelper.getUserByHttpServletRequest(httpServletRequest);
        Page<TourRequestResponse> tourRequestResponses=  tourRequestRepository.findAllByAuthenticatedGuestPage(pageable,user,q)
                .map(tourRequestMapper::tourRequestToResponse);

        return ResponseMessage.<Page<TourRequestResponse>>builder()
                .object(new RestPage<> (tourRequestResponses))
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("tour.requests.for.auth.user"))
                .build();

    }


    public ResponseMessage<Page<TourRequestResponse>> getAuthenticatedUsersAllOwnerTourRequest(String q, int page, int size, String sort, String type,
                                                                                               HttpServletRequest httpServletRequest) {

        Pageable pageable=  pageableHelper.getPageableWithProperties(page,size,sort,type);
        User user=  methodHelper.getUserByHttpServletRequest(httpServletRequest);
        Page<TourRequestResponse> tourRequestResponses=  tourRequestRepository.findAllByAuthenticatedOwnerPage(pageable,user,q)
                .map(tourRequestMapper::tourRequestToResponse);

        return ResponseMessage.<Page<TourRequestResponse>>builder()
                .object(new RestPage<> (tourRequestResponses))
                .httpStatus(HttpStatus.OK)
                .message(MessageUtil.getMessage("tour.requests.for.auth.user"))
                .build();


    }


    public RestPage<TourRequestResponse> getAuthenticatedUsersAllTourRequest(User user, int page, int size, String sort, String type) {
        Pageable pageable = pageableHelper.getPageableWithProperties(page, size, sort, type);
        Page<TourRequest> tourRequests = tourRequestRepository.findAllByGuestUserAndOwnerUser(user, pageable);
        List<TourRequestResponse> tourRequestResponses = tourRequests.getContent().stream()
                .map(tourRequestMapper::tourRequestToResponse)
                .collect(Collectors.toList());
        return new RestPage<>(tourRequestResponses, tourRequests.getNumber(), tourRequests.getSize(), tourRequests.getTotalElements());
    }

    private boolean isTourTimeValid (int minute) {
        return (minute == 00 || minute == 30 || minute==15 || minute==45);
    }

}
