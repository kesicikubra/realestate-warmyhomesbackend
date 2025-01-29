
package com.team03.service.helper;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.team03.RealEstateApplication;
import com.team03.entity.business.City;
import com.team03.entity.business.Country;
import com.team03.entity.business.District;
import com.team03.repository.business.CityRepository;
import com.team03.repository.business.CountryRepository;
import com.team03.repository.business.DistrictRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class  DataInitializer {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final DistrictRepository districtRepository;

    @Autowired
    public DataInitializer(CountryRepository countryRepository, CityRepository cityRepository, DistrictRepository districtRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
    }

    @PostConstruct
    @Transactional
    public void initializeData() {

        try {
            // Kontrol et ve varsa işlemleri yapma
            if (dataAlreadyLoaded()) {
                System.out.println("Data already loaded. Skipping initialization.");
                return;
            }

            // Veri yükleme işlemleri devam eder...
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = RealEstateApplication.class.getResourceAsStream("/CountryCityDistrictData.json");

            if (inputStream == null) {
                System.err.println("JsonData.json file not found.");
                return;
            }

            List<Country> countries = mapper.readValue(inputStream, new TypeReference<List<Country>>() {
            });

            // Toplu ekleme
            countryRepository.saveAll(countries);

            for (Country country : countries) {
                for (City city : country.getCities()) {
                    city.setCountry(country);
                }
                cityRepository.saveAll(country.getCities());
            }

            for (Country country : countries) {
                for (City city : country.getCities()) {
                    for (District district : city.getDistricts()) {
                        district.setCity(city);
                    }
                    districtRepository.saveAll(city.getDistricts());
                }
            }

            System.out.println("Data uploaded successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while loading data!: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean dataAlreadyLoaded() {
        // Herhangi bir tablonun boş olup olmadığını kontrol et
        return countryRepository.count() > 0 || cityRepository.count() > 0 || districtRepository.count() > 0;
    }


}





