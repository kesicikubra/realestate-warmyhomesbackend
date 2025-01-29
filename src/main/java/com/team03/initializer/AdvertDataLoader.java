package com.team03.initializer;

import com.team03.entity.business.*;
import com.team03.entity.enums.AdvertStatus;
import com.team03.repository.business.*;
import com.team03.repository.user.UserRepository;
import com.team03.service.helper.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdvertDataLoader {

    private final AdvertRepository advertRepository;
    private final ImageRepository imageRepository;
    private final AdvertsTypeRepository advertsTypeRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryPropertyValueDataLoader valueDataLoader;

    public void loadAdvert(){
        if (advertRepository.countAdvertByBuiltInTrue()==0){

            List<Advert> adverts = new ArrayList<>();

            Optional<AdvertType> advertType = advertsTypeRepository.findById(1L);

            Category houseCategory = categoryRepository.findById(1L).get();
            Category apartmentCategory = categoryRepository.findById(2L).get();
            Category officeCategory = categoryRepository.findById(3L).get();
            Category villaCategory = categoryRepository.findById(4L).get();
            LocalDateTime now = LocalDateTime.now();

// 1. İlan - Türkiye - apartment
            Advert apartment1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(2L).get())
                    .category(apartmentCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(40L).get()) // Istanbul
                    .district(districtRepository.findById(460L).get()) // Beyoglu
                    .title("Modern Apartment in Istanbul Center")
                    .description("Spacious apartment located in the heart of Istanbul, with modern amenities and great views.")
                    .slug("modern-apartment-istanbul-center")
                    .price(150000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(7)
                    .address("123 Main Street, Beyoglu")
                    .createAt(now)
                    .location(Location.builder().latitude("41.0082").longitude("28.9784").build())
                    .build();
            adverts.add(apartment1);

// 2. İlan - Norveç apartment
            Advert apartment2 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(2L).get())
                    .category(apartmentCategory)
                    .country(countryRepository.findById(2L).get()) // Norway
                    .city(cityRepository.findById(87L).get()) // Oslo
                    .district(districtRepository.findById(1204L).get()) // Sjølyststranda
                    .title("Cozy Apartment with Garden View in Oslo")
                    .description("Charming apartment with a lovely garden view, perfect for a peaceful living experience.")
                    .slug("cozy-apartment-oslo-garden-view")
                    .price(120000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(27)
                    .address("456 Elm Street, Sjølyststranda")
                    .createAt(now)
                    .location(Location.builder().latitude("59.9225").longitude("10.6731").build())
                    .build();
            adverts.add(apartment2);

// 3. İlan - Hollanda - apartment
            Advert apartment3 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(3L).get())
                    .category(apartmentCategory)
                    .country(countryRepository.findById(3L).get()) // Netherlands
                    .city(cityRepository.findById(103L).get()) // Amsterdam
                    .district(districtRepository.findById(2737L).get()) // Jordaan
                    .title("Luxury Penthouse with Panoramic City Views in Amsterdam")
                    .description("Exquisite penthouse offering luxury living and breathtaking city views from every room.")
                    .slug("luxury-penthouse-amsterdam-panoramic-views")
                    .price(300000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(35)
                    .address("789 Oak Avenue, Jordaan")
                    .createAt(now)
                    .location(Location.builder().latitude("52.3740").longitude("4.8897").build())
                    .build();
            adverts.add(apartment3);

// 4. İlan - Almanya
            Advert office1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(3L).get ())
                    .title("Modern Office Space in Business District")
                    .category(officeCategory)
                    .country(countryRepository.findById(4L).get()) // Germany
                    .city(cityRepository.findById(112L).get()) // Berlin
                    .district(districtRepository.findById(6055L).get()) // Mitte
                    .title("Modern Office Space in Berlin Business District")
                    .description("State-of-the-art office space located in the bustling business district, ideal for corporate headquarters.")
                    .slug("modern-office-berlin-business-district")
                    .price(250000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(29)
                    .address("101 Business Boulevard, Mitte")
                    .createAt(now)
                    .location(Location.builder().latitude("52.5200").longitude("13.4050").build())
                    .build();
            adverts.add(office1);

// 5. İlan - Birleşik Krallık
            Advert office2 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(4L).get())
                    .category(officeCategory)
                    .country(countryRepository.findById(5L).get()) // United Kingdom
                    .city(cityRepository.findById(213L).get()) // London
                    .district(districtRepository.findById(10395L).get()) // Abbey Wood
                    .title("Professional Office Suite with Parking in Abbey Wood")
                    .description("Professional office suite with ample parking, suitable for small businesses or startups.")
                    .slug("professional-office-suite-abbey-wood-parking")
                    .price(180000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(12)
                    .address("202 Commerce Street, Abbey Wood")
                    .createAt(now)
                    .location(Location.builder().latitude("51.4907").longitude("0.1203").build())
                    .build();
            adverts.add(office2);

// 6. İlan - Amerika Birleşik Devletleri
            // 6. İlan - Amerika Birleşik Devletleri (Spring Valley, Las Vegas)
            Advert villa1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(4L).get())
                    .category(villaCategory)
                    .country(countryRepository.findById(6L).get()) // United States
                    .city(cityRepository.findById(428L).get()) // Las Vegas
                    .district(districtRepository.findById(31881L).get()) // Spring Valley
                    .title("Luxury Villa with Private Pool in Spring Valley")
                    .description("Stunning villa featuring luxurious amenities and a private pool for ultimate relaxation and entertainment.")
                    .slug("luxury-villa-spring-valley-private-pool")
                    .price(500000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(9)
                    .address("1 Paradise Lane, Spring Valley")
                    .createAt(now)
                    .location(Location.builder().latitude("36.1147").longitude("-115.2450").build())
                    .build();
            adverts.add(villa1);

// 7. İlan - Fransa
            Advert villa2 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(5L).get())
                    .category(villaCategory)
                    .country(countryRepository.findById(7L).get()) // France
                    .city(cityRepository.findById(533L).get()) // Andard
                    .district(districtRepository.findById(41793L).get()) // Andard
                    .title("Secluded Mountain Retreat in Andard")
                    .description("Secluded villa nestled in the mountains, offering unparalleled tranquility and natural beauty.")
                    .slug("secluded-mountain-retreat-andard")
                    .price(400000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(236)
                    .address("2 Serenity Road, Andard")
                    .createAt(now)
                    .location(Location.builder().latitude("47.4644").longitude("-0.4035").build())
                    .build();
            adverts.add(villa2);

// 8. İlan - Birleşik Krallık
            Advert villa3 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(5L).get())
                    .category(villaCategory)
                    .country(countryRepository.findById(5L).get()) // United Kingdom
                    .city(cityRepository.findById(213L).get()) // Abram
                    .district(districtRepository.findById(10402L).get()) // Abram
                    .title("Contemporary Villa with Garden Views in Abram")
                    .description("Contemporary villa with sleek design and beautiful garden views, perfect for luxury living.")
                    .slug("contemporary-villa-abram-garden-views")
                    .price(600000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(23)
                    .address("3 Green Lane, Abram")
                    .createAt(now)
                    .location(Location.builder().latitude("53.5094").longitude("-2.5822").build())
                    .build();
            adverts.add(villa3);

// 9. İlan - Ankara (Apartment)
            Advert apartmentAnkara1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(6L).get())
                    .category(apartmentCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(7L).get()) // Ankara
                    .district(districtRepository.findById(72L).get()) // Cankaya
                    .title("Cozy Apartment in Çankaya")
                    .description("Comfortable and modern apartment located in the heart of Çankaya, Ankara.")
                    .slug("cozy-apartment-cankaya-ankara")
                    .price(250000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(15)
                    .address("456 Ataturk Blvd, Cankaya")
                    .createAt(now)
                    .location(Location.builder().latitude("39.9334").longitude("32.8597").build())
                    .build();
            adverts.add(apartmentAnkara1);

// 10. İlan - Ankara (Office)
            Advert officeAnkara1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(6L).get())
                    .category(officeCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(7L).get()) // Ankara
                    .district(districtRepository.findById(75L).get()) // Etimesgut
                    .title("Office Space in Etimesgut Business District")
                    .description("Well-located office space in the heart of Etimesgut's business district, perfect for corporate offices.")
                    .slug("office-space-etimesgut-ankara")
                    .price(300000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(20)
                    .address("789 Business Ave, Etimesgut")
                    .createAt(now)
                    .location(Location.builder().latitude("39.9208").longitude("32.6833").build())
                    .build();
            adverts.add(officeAnkara1);

// 11. İlan - Ankara (Villa)
            Advert villaAnkara1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(7L).get())
                    .category(villaCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(7L).get()) // Ankara
                    .district(districtRepository.findById(77L).get()) // Golbasi
                    .title("Luxury Villa with Private Pool in Gölbaşı")
                    .description("Luxurious villa with private pool and garden in the serene Gölbaşı area.")
                    .slug("luxury-villa-golbasi-ankara")
                    .price(450000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(10)
                    .address("101 Paradise Rd, Gölbaşı")
                    .createAt(now)
                    .location(Location.builder().latitude("39.7843").longitude("32.8038").build())
                    .build();
            adverts.add(villaAnkara1);

// 12. İlan - Ankara (House)
            Advert houseAnkara1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(7L).get())
                    .category(houseCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(7L).get()) // Ankara
                    .district(districtRepository.findById(75L).get()) // Etimesgut
                    .title("Charming House in Etimesgut")
                    .description("Charming house with a spacious garden in the family-friendly Etimesgut area.")
                    .slug("charming-house-etimesgut-ankara")
                    .price(200000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(18)
                    .address("202 Family St, Etimesgut")
                    .createAt(now)
                    .location(Location.builder().latitude("39.9208").longitude("32.6833").build())
                    .build();
            adverts.add(houseAnkara1);

// 13. İlan - Bursa (Apartment)
            Advert apartmentBursa1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(7L).get())
                    .category(apartmentCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(21L).get()) // Bursa
                    .district(districtRepository.findById(229L).get()) // Nilüfer
                    .title("Modern Apartment in Nilüfer")
                    .description("Spacious and modern apartment in the rapidly developing Nilüfer district of Bursa.")
                    .slug("modern-apartment-nilufer-bursa")
                    .price(220000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(12)
                    .address("123 Growth St, Nilüfer")
                    .createAt(now)
                    .location(Location.builder().latitude("40.2128").longitude("28.9718").build())
                    .build();
            adverts.add(apartmentBursa1);

// 14. İlan - Bursa (Office)
            Advert officeBursa1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(6L).get())
                    .category(officeCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(21L).get()) // Bursa
                    .district(districtRepository.findById(232L).get()) // Osmangazi
                    .title("Office Space in Osmangazi Business Hub")
                    .description("Well-located office space in the heart of Bursa's business hub, Osmangazi.")
                    .slug("office-space-osmangazi-bursa")
                    .price(270000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(25)
                    .address("456 Business Blvd, Osmangazi")
                    .createAt(now)
                    .location(Location.builder().latitude("40.1826").longitude("29.0661").build())
                    .build();
            adverts.add(officeBursa1);

// 15. İlan - Bursa (Villa)
            Advert villaBursa1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(5L).get())
                    .category(villaCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(21L).get()) // Bursa
                    .district(districtRepository.findById(227L).get()) // Mudanya
                    .title("Seaside Villa in Mudanya")
                    .description("Beautiful seaside villa with stunning views of the Marmara Sea in Mudanya, Bursa.")
                    .slug("seaside-villa-mudanya-bursa")
                    .price(500000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(30)
                    .address("789 Coastal Rd, Mudanya")
                    .createAt(now)
                    .location(Location.builder().latitude("40.3750").longitude("28.8823").build())
                    .build();
            adverts.add(villaBursa1);

// 16. İlan - Bursa (House)
            Advert houseBursa1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(4L).get())
                    .category(houseCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(21L).get()) // Bursa
                    .district(districtRepository.findById(234L).get()) // Yıldırım
                    .title("Traditional House in Yıldırım")
                    .description("Charming traditional house with modern upgrades in the historic Yıldırım district.")
                    .slug("traditional-house-yildirim-bursa")
                    .price(230000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(22)
                    .address("101 Heritage St, Yıldırım")
                    .createAt(now)
                    .location(Location.builder().latitude("40.2056").longitude("29.1183").build())
                    .build();
            adverts.add(houseBursa1);

// 17. İlan - İzmir (Apartment)
            Advert apartmentIzmir1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(3L).get())
                    .category(apartmentCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(41L).get()) // İzmir
                    .district(districtRepository.findById(495L).get()) // Çeşme
                    .title("Sea View Apartment in Çeşme")
                    .description("Modern apartment with a breathtaking sea view in Çeşme, İzmir.")
                    .slug("sea-view-apartment-cesme-izmir")
                    .price(280000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(18)
                    .address("123 Coastline St, Çeşme")
                    .createAt(now)
                    .location(Location.builder().latitude("38.3236").longitude("26.3154").build())
                    .build();
            adverts.add(apartmentIzmir1);

// 18. İlan - İzmir (Office)
            Advert officeIzmir1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(2L).get())
                    .category(officeCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(41L).get()) // İzmir
                    .district(districtRepository.findById(490L).get()) // Bayraklı
                    .title("Office Space in Bayraklı Business Center")
                    .description("High-quality office space in İzmir's Bayraklı business center, ideal for startups and established companies.")
                    .slug("office-space-bayrakli-izmir")
                    .price(320000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(26)
                    .address("456 Innovation Blvd, Bayraklı")
                    .createAt(now)
                    .location(Location.builder().latitude("38.4543").longitude("27.1673").build())
                    .build();
            adverts.add(officeIzmir1);

// 19. İlan - İzmir (Villa)
            Advert villaIzmir1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(2L).get())
                    .category(villaCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(41L).get()) // İzmir
                    .district(districtRepository.findById(516L).get()) // Urla
                    .title("Luxurious Villa in Urla")
                    .description("Magnificent villa with top-notch amenities and sea views in the picturesque town of Urla, İzmir.")
                    .slug("luxurious-villa-urla-izmir")
                    .price(550000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(35)
                    .address("789 Coastal Rd, Urla")
                    .createAt(now)
                    .location(Location.builder().latitude("38.3223").longitude("26.7640").build())
                    .build();
            adverts.add(villaIzmir1);

// 20. İlan - İzmir (House)
            Advert houseIzmir1 = Advert.builder()
                    .advertType(advertType.get())
                    .user(userRepository.findById(3L).get())
                    .category(houseCategory)
                    .country(countryRepository.findById(1L).get()) // Turkey
                    .city(cityRepository.findById(41L).get()) // İzmir
                    .district(districtRepository.findById(503L).get()) // Karşıyaka
                    .title("Modern House in Karşıyaka")
                    .description("Beautiful modern house located in the sought-after Karşıyaka district of İzmir.")
                    .slug("modern-house-karsiyaka-izmir")
                    .price(290000.0)
                    .status(AdvertStatus.ACTIVATED)
                    .builtIn(true)
                    .isActive(true)
                    .viewCount(21)
                    .address("202 Stylish St, Karşıyaka")
                    .createAt(now)
                    .location(Location.builder().latitude("38.4550").longitude("27.1047").build())
                    .build();
            adverts.add(houseIzmir1);

            advertRepository.saveAll(adverts);

            List<Image> images = new ArrayList<>();
            File imageApartment1 = new File("src/main/resources/images/apartment.jpg");
            File imageApartment2 = new File("src/main/resources/images/apartment-2.jpg");
            File imageApartment3 = new File("src/main/resources/images/apartment-3.jpg");
            File imageApartment4 = new File("src/main/resources/images/apartment-4.jpg");
            File imageApartment5 = new File("src/main/resources/images/apartment-5.jpg");
            File imageApartment6 = new File("src/main/resources/images/apartment-6.jpg");
            File imageOffice = new File("src/main/resources/images/office.jpg");
            File imageOffice2 = new File("src/main/resources/images/office-2.jpg");
            File imageOffice3 = new File("src/main/resources/images/office-3.jpg");
            File imageOffice4 = new File("src/main/resources/images/office-4.jpg");
            File imageOffice5 = new File("src/main/resources/images/office-5.jpg");
            File imageVilla = new File("src/main/resources/images/villa.jpg");
            File imageVilla2 = new File("src/main/resources/images/villa-2.jpg");
            File imageVilla3 = new File("src/main/resources/images/villa-3.jpg");
            File imageVilla4 = new File("src/main/resources/images/villa-4.jpg");
            File imageVilla5 = new File("src/main/resources/images/villa-5.jpg");
            File imageVilla6 = new File("src/main/resources/images/villa-6.jpg");
            File imageHouse1 = new File("src/main/resources/images/house-1.jpg");
            File imageHouse2 = new File("src/main/resources/images/house-2.jpg");
            File imageHouse3 = new File("src/main/resources/images/house-3.jpg");


            try {
                byte[] imgApartment1 = Files.readAllBytes(imageApartment1.toPath());
                byte[] imgApartment2 = Files.readAllBytes(imageApartment2.toPath());
                byte[] imgApartment3 = Files.readAllBytes(imageApartment3.toPath());
                byte[] imgApartment4 = Files.readAllBytes(imageApartment4.toPath());
                byte[] imgApartment5 = Files.readAllBytes(imageApartment5.toPath());
                byte[] imgApartment6 = Files.readAllBytes(imageApartment6.toPath());
                byte[] imgOffice1 = Files.readAllBytes(imageOffice.toPath());
                byte[] imgOffice2 = Files.readAllBytes(imageOffice2.toPath());
                byte[] imgOffice3 = Files.readAllBytes(imageOffice3.toPath());
                byte[] imgOffice4 = Files.readAllBytes(imageOffice4.toPath());
                byte[] imgOffice5 = Files.readAllBytes(imageOffice5.toPath());
                byte[] imgVilla1 = Files.readAllBytes(imageVilla.toPath());
                byte[] imgVilla2 = Files.readAllBytes(imageVilla2.toPath());
                byte[] imgVilla3 = Files.readAllBytes(imageVilla3.toPath());
                byte[] imgVilla4 = Files.readAllBytes(imageVilla4.toPath());
                byte[] imgVilla5 = Files.readAllBytes(imageVilla5.toPath());
                byte[] imgVilla6 = Files.readAllBytes(imageVilla6.toPath());
                byte[] imgHouse1 = Files.readAllBytes(imageHouse1.toPath());
                byte[] imgHouse2 = Files.readAllBytes(imageHouse2.toPath());
                byte[] imgHouse3 = Files.readAllBytes(imageHouse3.toPath());


                images.add(Image.builder().suitable(true).advert(advertRepository.findById(1L).get()).imageData(ImageUtils.compressImage(imgApartment1)).type("image/jpeg").featured(true).name("apartment.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(2L).get()).imageData(ImageUtils.compressImage(imgApartment2)).type("image/jpeg").featured(true).name("apartment.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(3L).get()).imageData(ImageUtils.compressImage(imgApartment3)).type("image/jpeg").featured(true).name("apartment.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(9L).get()).imageData(ImageUtils.compressImage(imgApartment4)).type("image/jpeg").featured(true).name("apartment.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(13L).get()).imageData(ImageUtils.compressImage(imgApartment5)).type("image/jpeg").featured(true).name("apartment.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(17L).get()).imageData(ImageUtils.compressImage(imgApartment6)).type("image/jpeg").featured(true).name("apartment.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(4L).get()).imageData(ImageUtils.compressImage(imgOffice1)).type("image/jpeg").featured(true).name("office.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(5L).get()).imageData(ImageUtils.compressImage(imgOffice2)).type("image/jpeg").featured(true).name("office.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(10L).get()).imageData(ImageUtils.compressImage(imgOffice3)).type("image/jpeg").featured(true).name("office.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(14L).get()).imageData(ImageUtils.compressImage(imgOffice4)).type("image/jpeg").featured(true).name("office.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(18L).get()).imageData(ImageUtils.compressImage(imgOffice5)).type("image/jpeg").featured(true).name("office.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(6L).get()).imageData(ImageUtils.compressImage(imgVilla1)).type("image/jpeg").featured(true).name("villa.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(7L).get()).imageData(ImageUtils.compressImage(imgVilla2)).type("image/jpeg").featured(true).name("villa.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(8L).get()).imageData(ImageUtils.compressImage(imgVilla3)).type("image/jpeg").featured(true).name("villa.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(11L).get()).imageData(ImageUtils.compressImage(imgVilla4)).type("image/jpeg").featured(true).name("villa.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(15L).get()).imageData(ImageUtils.compressImage(imgVilla5)).type("image/jpeg").featured(true).name("villa.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(19L).get()).imageData(ImageUtils.compressImage(imgVilla6)).type("image/jpeg").featured(true).name("villa.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(12L).get()).imageData(ImageUtils.compressImage(imgHouse1)).type("image/jpeg").featured(true).name("house.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(16L).get()).imageData(ImageUtils.compressImage(imgHouse2)).type("image/jpeg").featured(true).name("house.jpg").build());
                images.add(Image.builder().suitable(true).advert(advertRepository.findById(20L).get()).imageData(ImageUtils.compressImage(imgHouse3)).type("image/jpeg").featured(true).name("house.jpg").build());

                imageRepository.saveAll(images);
                valueDataLoader.loadCategoryPropertyValue();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
