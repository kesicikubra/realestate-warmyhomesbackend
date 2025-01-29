package com.team03.initializer;

import com.team03.entity.business.AdvertType;
import com.team03.repository.business.AdvertsTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdvertTypeDataLoader {

    private final AdvertsTypeRepository advertsTypeRepository;

    public void loadAdvertType(){
        if(advertsTypeRepository.countAdvertTypeByBuiltInTrue()==0){
            AdvertType builtInAdvertTypeForSale = new AdvertType().toBuilder()
                    .builtIn(true)
                    .title("for.sale")
                    .build();
            advertsTypeRepository.save(builtInAdvertTypeForSale);

            AdvertType builtInAdvertTypeForRent = new AdvertType().toBuilder()
                    .builtIn(true)
                    .title("for.rent")
                    .build();

            advertsTypeRepository.save(builtInAdvertTypeForRent);

            AdvertType builtInAdvertTypeForProject = new AdvertType().toBuilder()
                    .builtIn(true)
                    .title("renovation.project")
                    .build();

            advertsTypeRepository.save(builtInAdvertTypeForProject);

            AdvertType builtInAdvertTypeForDaily = new AdvertType().toBuilder()
                    .builtIn(true)
                    .title("daily.rental")
                    .build();

            advertsTypeRepository.save(builtInAdvertTypeForDaily);

            AdvertType builtInAdvertTypeForSeasonal = new AdvertType().toBuilder()
                    .builtIn(true)
                    .title("seasonal.rental")
                    .build();

            advertsTypeRepository.save(builtInAdvertTypeForSeasonal);

        }
    }
}