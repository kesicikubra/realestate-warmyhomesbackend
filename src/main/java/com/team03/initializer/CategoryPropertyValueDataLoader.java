package com.team03.initializer;

import com.team03.entity.business.Advert;
import com.team03.entity.business.CategoryPropertyKey;
import com.team03.entity.business.CategoryPropertyValue;
import com.team03.repository.business.AdvertRepository;
import com.team03.repository.business.CategoryPropertyKeyRepository;
import com.team03.repository.business.CategoryPropertyValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryPropertyValueDataLoader {

    private final CategoryPropertyValueRepository valueRepository;
    private final AdvertRepository advertRepository;
    private final CategoryPropertyKeyRepository keyRepository;

    public void loadCategoryPropertyValue(){

        Advert apartment1 = advertRepository.findById(1L).get();
        Advert apartment2 = advertRepository.findById(2L).get();
        Advert apartment3 = advertRepository.findById(3L).get();
        Advert office1 = advertRepository.findById(4L).get();
        Advert office2 = advertRepository.findById(5L).get();
        Advert villa1 = advertRepository.findById(6L).get();
        Advert villa2 = advertRepository.findById(7L).get();
        Advert villa3 = advertRepository.findById(8L).get();
        Advert apartment4 = advertRepository.findById(9L).get();
        Advert office3 = advertRepository.findById(10L).get();
        Advert villa4 = advertRepository.findById(11L).get();
        Advert house1 = advertRepository.findById(12L).get();
        Advert apartment5 = advertRepository.findById(13L).get();
        Advert office4 = advertRepository.findById(14L).get();
        Advert villa5 = advertRepository.findById(15L).get();
        Advert house2 = advertRepository.findById(16L).get();
        Advert apartment6 = advertRepository.findById(17L).get();
        Advert office5 = advertRepository.findById(18L).get();
        Advert villa6 = advertRepository.findById(19L).get();
        Advert house3 = advertRepository.findById(20L).get();

        // house key
        CategoryPropertyKey houseKeySize = keyRepository.findById(1L).get();
        CategoryPropertyKey houseBedrooms = keyRepository.findById(2L).get();
        CategoryPropertyKey houseBathrooms = keyRepository.findById(3L).get();
        CategoryPropertyKey houseYearOfBuild = keyRepository.findById(4L).get();
        CategoryPropertyKey houseFurniture = keyRepository.findById(5L).get();
        CategoryPropertyKey houseMaintenanceFee = keyRepository.findById(6L).get();
        CategoryPropertyKey houseGarage = keyRepository.findById(7L).get();
        CategoryPropertyKey houseTerrace = keyRepository.findById(8L).get();

        // apartment key
        CategoryPropertyKey apartmentKeySize = keyRepository.findById(9L).get();
        CategoryPropertyKey apartmentBedrooms = keyRepository.findById(10L).get();
        CategoryPropertyKey apartmentBathrooms = keyRepository.findById(11L).get();
        CategoryPropertyKey apartmentYearOfBuild = keyRepository.findById(12L).get();
        CategoryPropertyKey apartmentFurniture = keyRepository.findById(13L).get();
        CategoryPropertyKey apartmentMaintenanceFee = keyRepository.findById(14L).get();
        CategoryPropertyKey apartmentParkingSpace = keyRepository.findById(15L).get();
        CategoryPropertyKey apartmentBalcony = keyRepository.findById(16L).get();
        CategoryPropertyKey apartmentElevator = keyRepository.findById(17L).get();

        // office key
        CategoryPropertyKey officeKeySize = keyRepository.findById(18L).get();
        CategoryPropertyKey officeParkingSpace = keyRepository.findById(19L).get();
        CategoryPropertyKey officeYearOfBuild = keyRepository.findById(20L).get();
        CategoryPropertyKey officeFurniture = keyRepository.findById(21L).get();
        CategoryPropertyKey officeMaintenanceFee = keyRepository.findById(22L).get();
        CategoryPropertyKey officeElevator = keyRepository.findById(23L).get();


        // villa key
        CategoryPropertyKey villaKeySize = keyRepository.findById(24L).get();
        CategoryPropertyKey villaBedrooms = keyRepository.findById(25L).get();
        CategoryPropertyKey villaBathrooms = keyRepository.findById(26L).get();
        CategoryPropertyKey villaYearOfBuild = keyRepository.findById(27L).get();
        CategoryPropertyKey villaFurniture = keyRepository.findById(28L).get();
        CategoryPropertyKey villaMaintenanceFee = keyRepository.findById(29L).get();
        CategoryPropertyKey villaGarage = keyRepository.findById(30L).get();
        CategoryPropertyKey villaLandArea = keyRepository.findById(31L).get();
        CategoryPropertyKey villaView = keyRepository.findById(32L).get();
        CategoryPropertyKey villaSwimmingPool = keyRepository.findById(33L).get();

        List<CategoryPropertyValue> values = new ArrayList<>();


        // apartmetn category property value
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentKeySize).value("100").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentBedrooms).value("3").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentBathrooms).value("2").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentYearOfBuild).value("2000").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentMaintenanceFee).value("500").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentParkingSpace).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentBalcony).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment1).categoryPropertyKey(apartmentElevator).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentKeySize).value("120").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentBedrooms).value("2").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentBathrooms).value("1").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentYearOfBuild).value("1995").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentMaintenanceFee).value("300").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentParkingSpace).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentBalcony).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment2).categoryPropertyKey(apartmentElevator).value("false").build());

        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentKeySize).value("150").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentBedrooms).value("3").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentBathrooms).value("2").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentYearOfBuild).value("2005").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentMaintenanceFee).value("400").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentParkingSpace).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentBalcony).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(apartment3).categoryPropertyKey(apartmentElevator).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentKeySize).value("130").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentBedrooms).value("2").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentBathrooms).value("1").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentYearOfBuild).value("2000").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentMaintenanceFee).value("350").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentParkingSpace).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentBalcony).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment4).categoryPropertyKey(apartmentElevator).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentKeySize).value("140").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentBedrooms).value("3").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentBathrooms).value("2").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentYearOfBuild).value("1998").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentMaintenanceFee).value("320").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentParkingSpace).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentBalcony).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(apartment5).categoryPropertyKey(apartmentElevator).value("false").build());

        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentKeySize).value("110").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentBedrooms).value("1").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentBathrooms).value("1").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentYearOfBuild).value("2008").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentMaintenanceFee).value("250").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentParkingSpace).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentBalcony).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(apartment6).categoryPropertyKey(apartmentElevator).value("false").build());

        // house category property value
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseKeySize).value("300").build());
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseBedrooms).value("3").build());
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseBathrooms).value("1").build());
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseYearOfBuild).value("2020").build());
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseMaintenanceFee).value("600").build());
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseGarage).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(house1).categoryPropertyKey(houseTerrace).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseKeySize).value("280").build());
        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseBedrooms).value("4").build());
        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseBathrooms).value("2").build());
        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseYearOfBuild).value("2015").build());
        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseMaintenanceFee).value("700").build());
        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseGarage).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(house2).categoryPropertyKey(houseTerrace).value("false").build());

        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseKeySize).value("350").build());
        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseBedrooms).value("5").build());
        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseBathrooms).value("3").build());
        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseYearOfBuild).value("2010").build());
        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseMaintenanceFee).value("800").build());
        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseGarage).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(house3).categoryPropertyKey(houseTerrace).value("true").build());


        // Villa category property value

        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaKeySize).value("100").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaBedrooms).value("6").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaBathrooms).value("2").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaYearOfBuild).value("2010").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaMaintenanceFee).value("900").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaGarage).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaLandArea).value("800").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaView).value("Mountain").build());
        values.add(CategoryPropertyValue.builder().advert(villa1).categoryPropertyKey(villaSwimmingPool).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaKeySize).value("150").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaBedrooms).value("5").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaBathrooms).value("3").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaYearOfBuild).value("2005").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaMaintenanceFee).value("1000").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaGarage).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaLandArea).value("1000").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaView).value("Sea").build());
        values.add(CategoryPropertyValue.builder().advert(villa2).categoryPropertyKey(villaSwimmingPool).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaKeySize).value("200").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaBedrooms).value("4").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaBathrooms).value("4").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaYearOfBuild).value("2012").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaMaintenanceFee).value("1200").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaGarage).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaLandArea).value("1200").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaView).value("Mountain").build());
        values.add(CategoryPropertyValue.builder().advert(villa3).categoryPropertyKey(villaSwimmingPool).value("false").build());


        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaKeySize).value("180").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaBedrooms).value("6").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaBathrooms).value("5").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaYearOfBuild).value("2015").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaMaintenanceFee).value("1100").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaGarage).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaLandArea).value("900").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaView).value("Sea").build());
        values.add(CategoryPropertyValue.builder().advert(villa4).categoryPropertyKey(villaSwimmingPool).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaKeySize).value("220").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaBedrooms).value("4").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaBathrooms).value("3").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaYearOfBuild).value("2018").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaMaintenanceFee).value("1300").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaGarage).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaLandArea).value("1100").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaView).value("Mountain").build());
        values.add(CategoryPropertyValue.builder().advert(villa5).categoryPropertyKey(villaSwimmingPool).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaKeySize).value("250").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaBedrooms).value("5").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaBathrooms).value("4").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaYearOfBuild).value("2020").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaMaintenanceFee).value("1400").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaGarage).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaLandArea).value("1300").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaView).value("Sea").build());
        values.add(CategoryPropertyValue.builder().advert(villa6).categoryPropertyKey(villaSwimmingPool).value("true").build());

        // Office category property value
        values.add(CategoryPropertyValue.builder().advert(office1).categoryPropertyKey(officeKeySize).value("90").build());
        values.add(CategoryPropertyValue.builder().advert(office1).categoryPropertyKey(officeParkingSpace).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(office1).categoryPropertyKey(officeYearOfBuild).value("2005").build());
        values.add(CategoryPropertyValue.builder().advert(office1).categoryPropertyKey(officeFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(office1).categoryPropertyKey(officeMaintenanceFee).value("800").build());
        values.add(CategoryPropertyValue.builder().advert(office1).categoryPropertyKey(officeElevator).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(office2).categoryPropertyKey(officeKeySize).value("100").build());
        values.add(CategoryPropertyValue.builder().advert(office2).categoryPropertyKey(officeParkingSpace).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(office2).categoryPropertyKey(officeYearOfBuild).value("2010").build());
        values.add(CategoryPropertyValue.builder().advert(office2).categoryPropertyKey(officeFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(office2).categoryPropertyKey(officeMaintenanceFee).value("900").build());
        values.add(CategoryPropertyValue.builder().advert(office2).categoryPropertyKey(officeElevator).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(office3).categoryPropertyKey(officeKeySize).value("120").build());
        values.add(CategoryPropertyValue.builder().advert(office3).categoryPropertyKey(officeParkingSpace).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(office3).categoryPropertyKey(officeYearOfBuild).value("2015").build());
        values.add(CategoryPropertyValue.builder().advert(office3).categoryPropertyKey(officeFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(office3).categoryPropertyKey(officeMaintenanceFee).value("1000").build());
        values.add(CategoryPropertyValue.builder().advert(office3).categoryPropertyKey(officeElevator).value("false").build());

        values.add(CategoryPropertyValue.builder().advert(office4).categoryPropertyKey(officeKeySize).value("150").build());
        values.add(CategoryPropertyValue.builder().advert(office4).categoryPropertyKey(officeParkingSpace).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(office4).categoryPropertyKey(officeYearOfBuild).value("2018").build());
        values.add(CategoryPropertyValue.builder().advert(office4).categoryPropertyKey(officeFurniture).value("true").build());
        values.add(CategoryPropertyValue.builder().advert(office4).categoryPropertyKey(officeMaintenanceFee).value("1100").build());
        values.add(CategoryPropertyValue.builder().advert(office4).categoryPropertyKey(officeElevator).value("true").build());

        values.add(CategoryPropertyValue.builder().advert(office5).categoryPropertyKey(officeKeySize).value("80").build());
        values.add(CategoryPropertyValue.builder().advert(office5).categoryPropertyKey(officeParkingSpace).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(office5).categoryPropertyKey(officeYearOfBuild).value("2012").build());
        values.add(CategoryPropertyValue.builder().advert(office5).categoryPropertyKey(officeFurniture).value("false").build());
        values.add(CategoryPropertyValue.builder().advert(office5).categoryPropertyKey(officeMaintenanceFee).value("950").build());
        values.add(CategoryPropertyValue.builder().advert(office5).categoryPropertyKey(officeElevator).value("true").build());

        valueRepository.saveAll(values);
    }
}