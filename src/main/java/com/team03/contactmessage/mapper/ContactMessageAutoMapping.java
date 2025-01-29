package com.team03.contactmessage.mapper;

import com.team03.contactmessage.dto.ContactMessageRequest;
import com.team03.contactmessage.dto.ContactMessageResponse;
import com.team03.contactmessage.entity.ContactMessage;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public abstract  class ContactMessageAutoMapping {

    /*
    NOT: projeyi calistirmdan once .jar olusturmaniz ( sag menudeki M de " mvn cleane install " yazarak .jar dosyasi olusturulur)
     gerekmektedir daha sonra  proje run edilir o zaman aotu mapplama gerceklesir.
     Nasil yapildigini gormek icin @Mapper annotation sirasindaki " inheritor" a basarak o classa gidebilirsiniz
     */

    //@Mapping(target = "id",ignore = true)  bu yontem ile mappalamak istemedigimiz filedleri belirtiyoruz
    public abstract ContactMessage requestToContactMessage(ContactMessageRequest contactMessageRequest);


    public abstract ContactMessageResponse contactMessageToResponse(ContactMessage contactMessage);

}
