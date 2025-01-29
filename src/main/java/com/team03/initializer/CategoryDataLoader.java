package com.team03.initializer;

import com.team03.entity.business.Category;
import com.team03.entity.business.CategoryPropertyKey;
import com.team03.entity.enums.KeyType;
import com.team03.repository.business.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CategoryDataLoader {

    private final CategoryRepository categoryRepository;

    public void loadCategoryAndKey(){

        if(categoryRepository.countCategoryByBuiltInTrue()==0){

            ///////////////////////// House ////////////////////////

            Set<CategoryPropertyKey> categoryPropertyKeysHouse = new LinkedHashSet<>();

            BaseKeys(categoryPropertyKeysHouse);

            categoryPropertyKeysHouse.add(CategoryPropertyKey.builder()
                    .name("garage")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());

            categoryPropertyKeysHouse.add(CategoryPropertyKey.builder()
                    .name("terrace")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());

            Category builtInHouse = new Category().toBuilder()
                    .title("house")
                    .seq(1)
                    .icon("faHome")
                    .builtIn(true)
                    .isActive(true)
                    .createAt(LocalDateTime.now())
                    .slug("house")
                    .categoryPropertyKeys(categoryPropertyKeysHouse)
                    .build();


            categoryRepository.save(builtInHouse);
            //////////////////////  Apartment  //////////////////////

            Set<CategoryPropertyKey> categoryPropertyKeysApartment = new LinkedHashSet<>();

            BaseKeys(categoryPropertyKeysApartment);

            categoryPropertyKeysApartment.add(CategoryPropertyKey.builder()
                    .name("parking.space")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());

            categoryPropertyKeysApartment.add(CategoryPropertyKey.builder()
                    .name("balcony")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());
            categoryPropertyKeysApartment.add(CategoryPropertyKey.builder()
                    .name("elevator")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());

            Category builtInApartment = new Category().toBuilder()
                    .title("apartment")
                    .seq(2)
                    .icon("faBuilding")
                    .builtIn(true)
                    .isActive(true)
                    .createAt(LocalDateTime.now())
                    .slug("apartment")
                    .categoryPropertyKeys(categoryPropertyKeysApartment)
                    .build();
            categoryRepository.save(builtInApartment);

            //////////////////////  Office //////////////////////
            Set<CategoryPropertyKey> categoryPropertyKeysOffice =new LinkedHashSet<>();

            categoryPropertyKeysOffice.add(CategoryPropertyKey.builder()
                    .name("category.key.size")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .unit("m²")
                    .build());
            categoryPropertyKeysOffice.add(CategoryPropertyKey.builder()
                    .name("parking.space")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());
            categoryPropertyKeysOffice.add(CategoryPropertyKey.builder()
                    .name("year.of.build")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .build());
            categoryPropertyKeysOffice.add(CategoryPropertyKey.builder()
                    .name("furniture")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());
            categoryPropertyKeysOffice.add(CategoryPropertyKey.builder()
                    .name("maintenance.fee")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .unit("$")
                    .build());
            categoryPropertyKeysOffice.add(CategoryPropertyKey.builder()
                    .name("elevator")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());

            Category builtInOffice = new Category().toBuilder()
                    .title("office")
                    .seq(3)
                    .icon("faHouseLaptop")
                    .builtIn(true)
                    .isActive(true)
                    .createAt(LocalDateTime.now())
                    .slug("office")
                    .categoryPropertyKeys(categoryPropertyKeysOffice)
                    .build();
            categoryRepository.save(builtInOffice);

            //////////////////////  Villa  //////////////////////
            Set<CategoryPropertyKey> categoryPropertyKeysVilla=new LinkedHashSet<>();

            BaseKeys(categoryPropertyKeysVilla);

            categoryPropertyKeysVilla.add(CategoryPropertyKey.builder()
                    .name("garage")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());
            categoryPropertyKeysVilla.add(CategoryPropertyKey.builder()
                    .name("land.area")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .unit("m²")
                    .build());

            categoryPropertyKeysVilla.add(CategoryPropertyKey.builder()
                    .name("view")
                    .builtIn(true)
                    .keyType(KeyType.TEXT)
                    .build());

            categoryPropertyKeysVilla.add(CategoryPropertyKey.builder()
                    .name("swimming.pool")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());

            Category builtInVilla = new Category().toBuilder()
                    .title("villa")
                    .seq(4)
                    .icon("faHouseChimneyWindow")
                    .builtIn(true)
                    .isActive(true)
                    .createAt(LocalDateTime.now())
                    .slug("villa")
                    .categoryPropertyKeys(categoryPropertyKeysVilla)
                    .build();
            categoryRepository.save(builtInVilla);
            //////////////////////  Land  //////////////////////
            Set<CategoryPropertyKey> categoryPropertyKeysLand=new LinkedHashSet<>();


            categoryPropertyKeysLand.add(CategoryPropertyKey.builder()
                    .name("category.key.size")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .unit("m²")
                    .build());
            categoryPropertyKeysLand.add(CategoryPropertyKey.builder()
                    .name("zoning")
                    .builtIn(true)
                    .keyType(KeyType.TEXT)
                    .build());
            categoryPropertyKeysLand.add(CategoryPropertyKey.builder()
                    .name("access.road")
                    .builtIn(true)
                    .keyType(KeyType.TEXT)
                    .build());
            categoryPropertyKeysLand.add(CategoryPropertyKey.builder()
                    .name("legal.status")
                    .builtIn(true)
                    .keyType(KeyType.TEXT)
                    .build());


            Category builtInLand = new Category().toBuilder()
                    .title("land")
                    .seq(5)
                    .icon("faImage")
                    .builtIn(true)
                    .isActive(true)
                    .createAt(LocalDateTime.now())
                    .slug("land")
                    .categoryPropertyKeys(categoryPropertyKeysLand)
                    .build();
            categoryRepository.save(builtInLand);
            //////////////////////  Shop //////////////////////

            Set<CategoryPropertyKey> categoryPropertyKeysShop =new LinkedHashSet<>();

            categoryPropertyKeysShop.add(CategoryPropertyKey.builder()
                    .name("category.key.size")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .unit("m²")
                    .build());
            categoryPropertyKeysShop.add(CategoryPropertyKey.builder()
                    .name("parking.space")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());
            categoryPropertyKeysShop.add(CategoryPropertyKey.builder()
                    .name("year.of.build")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .build());
            categoryPropertyKeysShop.add(CategoryPropertyKey.builder()
                    .name("furniture")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());
            categoryPropertyKeysShop.add(CategoryPropertyKey.builder()
                    .name("maintenance.fee")
                    .builtIn(true)
                    .keyType(KeyType.NUMBER)
                    .unit("$")
                    .build());
            categoryPropertyKeysShop.add(CategoryPropertyKey.builder()
                    .name("elevator")
                    .builtIn(true)
                    .keyType(KeyType.BOOLEAN)
                    .build());


            Category builtInShop = new Category().toBuilder()
                    .title("shop")
                    .seq(6)
                    .icon("faStore")
                    .builtIn(true)
                    .isActive(true)
                    .createAt(LocalDateTime.now())
                    .slug("shop")
                    .categoryPropertyKeys(categoryPropertyKeysShop)
                    .build();
            categoryRepository.save(builtInShop);

        }
    }

    private void BaseKeys(Set<CategoryPropertyKey> categoryPropertyKeys) {
        categoryPropertyKeys.add(CategoryPropertyKey.builder()
                .name("category.key.size")
                .builtIn(true)
                .keyType(KeyType.NUMBER)
                .unit("m²")
                .build());
        categoryPropertyKeys.add(CategoryPropertyKey.builder()
                .name("bedrooms")
                .builtIn(true)
                .keyType(KeyType.NUMBER)
                .build());
        categoryPropertyKeys.add(CategoryPropertyKey.builder()
                .name("bathrooms")
                .builtIn(true)
                .keyType(KeyType.NUMBER)
                .build());
        categoryPropertyKeys.add(CategoryPropertyKey.builder()
                .name("year.of.build")
                .builtIn(true)
                .keyType(KeyType.NUMBER)
                .build());
        categoryPropertyKeys.add(CategoryPropertyKey.builder()
                .name("furniture")
                .builtIn(true)
                .keyType(KeyType.BOOLEAN)
                .build());
        categoryPropertyKeys.add(CategoryPropertyKey.builder()
                .name("maintenance.fee")
                .builtIn(true)
                .keyType(KeyType.NUMBER)
                .unit("$")
                .build());
    }
}
