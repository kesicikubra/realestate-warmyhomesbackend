package com.team03.service.business;

import com.team03.entity.business.AdvertType;
import com.team03.exception.BadRequestException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.i18n.MessageUtil;
import com.team03.payload.mappers.AdvertTypeMapper;
import com.team03.payload.request.business.AdvertTypeRequest;
import com.team03.payload.response.business.AdvertTypeResponse;
import com.team03.payload.response.business.ResponseMessage;
import com.team03.repository.business.AdvertRepository;
import com.team03.repository.business.AdvertsTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdvertTypeService {

    private final AdvertsTypeRepository advertsTypeRepository;
    private final AdvertTypeMapper advertTypeMapper;
    private final AdvertRepository advertRepository;


    public AdvertType getAdvertTypeById(Long id) {
        return advertsTypeRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(MessageUtil.getMessage("not.found.advert.type", id)));
    }


    //post
  //  @CachePut(cacheNames = "advert-types", key = "#result.object.id")
    public ResponseMessage<AdvertTypeResponse> saveAdvertType(AdvertTypeRequest advertTypeRequest) {
        //uniq control
        getAdvertTypeByTitle(advertTypeRequest.getTitle());
        AdvertType advertType = advertTypeMapper.requestToAdvertType(advertTypeRequest);
        AdvertType savedAdvertType = advertsTypeRepository.save(advertType);
        return ResponseMessage.<AdvertTypeResponse>builder()
                .message("Advert Type created successfully")
                .httpStatus(HttpStatus.CREATED)
                .object(advertTypeMapper.responseToAdvertType(savedAdvertType))
                .build();
    }


  //  @Cacheable(cacheNames = "advert-types")
    public ResponseMessage<List<AdvertTypeResponse>> getAdvertTypes() {

        List<AdvertType> advertTypes = advertsTypeRepository.findAll();
        if (advertTypes.isEmpty()) {
            return ResponseMessage.<List<AdvertTypeResponse>>builder()
                    .httpStatus(HttpStatus.NO_CONTENT)
                    .message("Advert Type not found")
                    .build();
        }

        return ResponseMessage.<List<AdvertTypeResponse>>builder()
                .httpStatus(HttpStatus.OK)
                .object(advertTypes.stream().map(advertTypeMapper::responseToAdvertType).toList())
                .build();
    }


  //  @Cacheable(value = "advert-types", key = "#id")
    public ResponseMessage<AdvertTypeResponse> getById(Long id) {
        AdvertType advertType = getAdvertTypeById(id); //yoksa hata
        AdvertTypeResponse response = advertTypeMapper.responseToAdvertType(advertType);
        return ResponseMessage.<AdvertTypeResponse>builder()
                .httpStatus(HttpStatus.OK)
                .object(response)
                .build();
    }

    public ResponseMessage<AdvertTypeResponse> updateAdvertTypeById(Long id, AdvertTypeRequest advertTypeRequest) {
        AdvertType advertType=getAdvertTypeById(id); //yoksa hata
        if(advertType.getBuiltIn()){
            throw new BadRequestException(MessageUtil.getMessage("built.in.value.error"));
        }
        getAdvertTypeByTitle(advertTypeRequest.getTitle()); // uniq isim girmeli yoksa hata
        AdvertType advertType1 = advertTypeMapper.requestToAdvertType(advertTypeRequest);
        advertType1.setId(advertType.getId());
        AdvertType savedAdvertType = advertsTypeRepository.save(advertType1);
        return ResponseMessage.<AdvertTypeResponse>builder()
                .message("Advert Type Updated Successfully")
                .httpStatus(HttpStatus.OK)
                .object(advertTypeMapper.responseToAdvertType(savedAdvertType))
                .build();
    }


  //  @CacheEvict(value = "advert-types", key = "#id")
    public ResponseMessage<AdvertTypeResponse> deleteAdvertTypeById(Long id) {
        AdvertType advertType=getAdvertTypeById(id); //yoksa hata
        if(advertType.getBuiltIn()){
            throw new BadRequestException(MessageUtil.getMessage("built.in.value.error"));
        }
        Boolean advert=advertRepository.existsByAdvert_typeId(id);
        if(advert){
            throw new BadRequestException(MessageUtil.getMessage("associated.delete.error"));
        }
        AdvertTypeResponse advertTypeResponse=advertTypeMapper.responseToAdvertType(advertType);
        advertsTypeRepository.deleteById(id);
        return ResponseMessage.<AdvertTypeResponse>builder()
                .message("Advert Type Deleted Successfully")
                .httpStatus(HttpStatus.OK)
                .object(advertTypeResponse)
                .build();

    }

    public void getAdvertTypeByTitle(String title) {
        if (advertsTypeRepository.findByTitle(title) != null) {
            throw new BadRequestException(MessageUtil.getMessage("already.exist", title));
        }
    }
}
