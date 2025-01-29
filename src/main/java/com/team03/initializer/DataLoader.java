package com.team03.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final UserDataLoader userDataLoader;
    private final CategoryDataLoader categoryDataLoader;
    private final AdvertTypeDataLoader advertTypeDataLoader;
    private final AdvertDataLoader advertDataLoader;

    public void allDataLoader(){
        userDataLoader.loadBuiltInUserAndRole();
        categoryDataLoader.loadCategoryAndKey();
        advertTypeDataLoader.loadAdvertType();
        advertDataLoader.loadAdvert();
    }
}
