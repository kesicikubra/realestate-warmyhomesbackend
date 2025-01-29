package com.team03.service.bussines;

import com.team03.entity.business.Category;
import com.team03.exception.BadRequestException;
import com.team03.exception.ResourceNotFoundException;
import com.team03.payload.mappers.CategoryMapper;
import com.team03.payload.request.business.CategoryRequest;
import com.team03.payload.request.business.CategoryUpdateRequest;
import com.team03.payload.response.business.CategoryResponseWithoutPropertyKey;
import com.team03.payload.response.business.CategoryUpdateResponse;
import com.team03.repository.business.CategoryRepository;
import com.team03.service.business.CategoryService;
import com.team03.service.helper.SlugHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private SlugHelper slugHelper;

    @InjectMocks
    private CategoryService categoryService;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testSaveCategory() {
        // Arrange
        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setTitle("Test Category");
        categoryRequest.setIcon("test-icon.png");
        categoryRequest.setSeq(1);
        categoryRequest.setIsActive(true);

        Category category = Category.builder()
                .title("Test Category")
                .slug("test-category")
                .icon("test-icon.png")
                .seq(1)
                .isActive(true)
                .createAt(LocalDateTime.now())
                .builtIn(false)
                .categoryPropertyKeys(new HashSet<>())
                .build();

        when(categoryRepository.existsByTitle("Test Category")).thenReturn(false);
        when(categoryMapper.requestToCategory(any(CategoryRequest.class))).thenReturn(category);
        when(slugHelper.createSlug("Test Category")).thenReturn("test-category");

        Category savedCategory = Category.builder()
                .id(1L)
                .title(category.getTitle())
                .icon(category.getIcon())
                .seq(category.getSeq())
                .slug(category.getSlug())
                .isActive(category.getIsActive())
                .createAt(category.getCreateAt())
                .updateAt(LocalDateTime.now())
                .builtIn(category.getBuiltIn())
                .categoryPropertyKeys(category.getCategoryPropertyKeys())
                .build();

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        CategoryResponseWithoutPropertyKey responseMock = CategoryResponseWithoutPropertyKey.builder()
                .id(1L)
                .title("Test Category")
                .icon("test-icon.png")
                .slug("test-category")
                .seq(1)
                .isActive(true)
                .createAt(savedCategory.getCreateAt())
                .build();

        when(categoryMapper.CategoryToWithoutPropertyKeyResponse(savedCategory)).thenReturn(responseMock);



        // Act
        var response = categoryService.saveCategory(categoryRequest);

        // Assert
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getObject(), "Response object should not be null");

        // Extra validations
        assertEquals(HttpStatus.CREATED, response.getHttpStatus(), "HttpStatus should be CREATED");
        assertEquals("Test Category", response.getObject().getTitle(), "Title should match");
        assertEquals("test-category", response.getObject().getSlug(), "Slug should match");
        assertEquals("test-icon.png", response.getObject().getIcon(), "Icon should match");
        assertEquals(1, response.getObject().getSeq(), "Sequence should match");
        assertTrue(response.getObject().getIsActive(), "IsActive should be true");
        assertEquals(savedCategory.getCreateAt(), response.getObject().getCreateAt(), "CreateAt should match");

        // Verify that the mocks were called correctly
        verify(categoryRepository).existsByTitle("Test Category");
        verify(categoryMapper).requestToCategory(any(CategoryRequest.class));
        verify(slugHelper).createSlug("Test Category");
        verify(categoryRepository).save(any(Category.class));
        verify(categoryMapper).CategoryToWithoutPropertyKeyResponse(savedCategory);
    }

    @Test
    void testUpdateById() {
        // Arrange
        Long categoryId = 1L;

        CategoryUpdateRequest updateRequest = CategoryUpdateRequest.builder()
                .title("Villa")
                .icon("fa villa")
                .seq(15)
                .isActive(false)
                .build();

        Category existingCategory = Category.builder()
                .id(categoryId)
                .title("Test Category")
                .slug("test-category")
                .icon("test-icon.png")
                .seq(1)
                .isActive(true)
                .createAt(LocalDateTime.of(2024, 8, 16, 14, 49, 0))
                .builtIn(false)
                .categoryPropertyKeys(new HashSet<>())
                .build();

        Category updatedCategory = Category.builder()
                .id(categoryId)
                .title("Villa")
                .slug("villa")
                .icon("fa villa")
                .seq(15)
                .isActive(false)
                .createAt(existingCategory.getCreateAt())
                .updateAt(LocalDateTime.now())
                .builtIn(false)
                .categoryPropertyKeys(new HashSet<>())
                .build();

        CategoryUpdateResponse updateResponse = CategoryUpdateResponse.builder()
                .id(categoryId)
                .title("Villa")
                .slug("villa")
                .icon("fa villa")
                .seq(15)
                .isActive(false)
                .createAt(existingCategory.getCreateAt())
                .updateAt(updatedCategory.getUpdateAt())
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryMapper.requestToUpdateCategory(updateRequest)).thenReturn(updatedCategory);
        when(slugHelper.createSlug(updateRequest.getTitle())).thenReturn("villa");
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.updateCategoryToResponse(updatedCategory)).thenReturn(updateResponse);

        // Act
        var response = categoryService.updateById(categoryId, updateRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals("Villa", response.getObject().getTitle());
        assertEquals("villa", response.getObject().getSlug());
        assertEquals("fa villa", response.getObject().getIcon());
        assertEquals(15, response.getObject().getSeq());
        assertEquals(updatedCategory.getUpdateAt(), response.getObject().getUpdateAt());

        // Verify that the mocks were called correctly
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).requestToUpdateCategory(updateRequest);
        verify(slugHelper).createSlug("Villa");
        verify(categoryRepository).save(updatedCategory);
        verify(categoryMapper).updateCategoryToResponse(updatedCategory);

        // No more interactions with mocks
        verifyNoMoreInteractions(categoryRepository, categoryMapper, slugHelper);
    }

    @Test
    void testUpdateById_CategoryNotFound() {
        // Arrange
        Long categoryId = 1L;
        CategoryUpdateRequest updateRequest = CategoryUpdateRequest.builder()
                .title("Villa")
                .icon("fa villa")
                .seq(15)
                .isActive(false)
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateById(categoryId, updateRequest);
        });

        // Verify that no further interactions happened
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper, slugHelper);
    }

    @Test
    void testUpdateById_BuiltInCategory() {
        // Arrange
        Long categoryId = 1L;
        CategoryUpdateRequest updateRequest = CategoryUpdateRequest.builder()
                .title("Villa")
                .icon("fa villa")
                .seq(15)
                .isActive(false)
                .build();

        Category existingCategory = Category.builder()
                .id(categoryId)
                .title("Test Category")
                .slug("test-category")
                .icon("test-icon.png")
                .seq(1)
                .isActive(true)
                .createAt(LocalDateTime.of(2024, 8, 16, 14, 49, 0))
                .builtIn(true)  // builtIn değeri true
                .categoryPropertyKeys(new HashSet<>())
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            categoryService.updateById(categoryId, updateRequest);
        });

        // Verify that no further interactions happened
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper, slugHelper);
    }

    @Test
    void testUpdateById_NullOrEmptyTitle() {
        // Arrange
        Long categoryId = 1L;
        CategoryUpdateRequest updateRequestWithNullTitle = CategoryUpdateRequest.builder()
                .title(null)  // Title null
                .icon("fa villa")
                .seq(15)
                .isActive(false)
                .build();

        CategoryUpdateRequest updateRequestWithEmptyTitle = CategoryUpdateRequest.builder()
                .title("")  // Title boş string
                .icon("fa villa")
                .seq(15)
                .isActive(false)
                .build();

        Category existingCategory = Category.builder()
                .id(categoryId)
                .title("Test Category")
                .slug("test-category")
                .icon("test-icon.png")
                .seq(1)
                .isActive(true)
                .createAt(LocalDateTime.of(2024, 8, 16, 14, 49, 0))
                .builtIn(false)
                .categoryPropertyKeys(new HashSet<>())
                .build();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        // Act & Assert for null title
        assertThrows(BadRequestException.class, () -> {
            categoryService.updateById(categoryId, updateRequestWithNullTitle);
        });

        // Act & Assert for empty title
        assertThrows(BadRequestException.class, () -> {
            categoryService.updateById(categoryId, updateRequestWithEmptyTitle);
        });

        // Verify that no further interactions happened
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper, slugHelper);
    }



}
