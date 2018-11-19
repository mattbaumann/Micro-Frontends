package ch.hsr.apparch.recipe.service;

import ch.hsr.apparch.recipe.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag("service")
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CategoryServiceTest {
    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService service;

    private static Stream<GenerateTestData.TestDataHolder> createTestData() {
        return GenerateTestData.createTestData();
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        Mockito.clearInvocations(recipeRepository, categoryRepository);
    }

    @Test
    void testCategoriesDI() {
        assertThat(categoryRepository).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testListCategory(GenerateTestData.TestDataHolder holder) {
        Mockito.when(categoryRepository.findAll(Sort.by(Sort.Order.asc(Recipe.NAME_PROPERTY))))
                .thenReturn(Collections.singletonList(holder.getCategory()));

        assertThat(service.listCategories()).containsExactly(holder.getCategory());

        Mockito.verify(categoryRepository).findAll(Sort.by(Sort.Order.asc(Recipe.NAME_PROPERTY)));
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testFindKnownCategoryOrNew(GenerateTestData.TestDataHolder holder) {
        Mockito.when(categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));

        assertThat(service.findOrNew(Optional.of(holder.getCategoryId()))).isEqualTo(holder.getCategory());

        Mockito.verify(categoryRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testFindUnknownCategoryOrNew(GenerateTestData.TestDataHolder holder) {
        Mockito.when(categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.empty());

        assertThat(service.findOrNew(Optional.of(holder.getCategoryId()))).isEqualTo(new Category());

        Mockito.verify(categoryRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testAddCategory(GenerateTestData.TestDataHolder holder) {
        Category category = new Category(holder.getCategory().getName(), Collections.emptyList());
        Mockito.when(this.categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(category));
        Mockito.when(this.categoryRepository.save(category)).thenReturn(category);

        assertThat(service.add(holder.getCategory().getName())).isEqualTo(category);

        Mockito.verify(this.categoryRepository).save(category);
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testUpdateUnknownCategory(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.empty());
        Mockito.when(this.categoryRepository.save(holder.getCategory())).thenReturn(holder.getCategory());

        assertThatThrownBy(() -> service.update(holder.getCategoryId(), holder.getCategory().getName()))
                .isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(this.categoryRepository).findById(holder.getCategoryId());
        Mockito.verify(this.categoryRepository, Mockito.never()).save(holder.getCategory());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testUpdateCategory(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));
        Mockito.when(this.categoryRepository.save(holder.getCategory())).thenReturn(holder.getCategory());

        assertThat(service.update(holder.getCategoryId(), holder.getCategory().getName())).isEqualTo(holder.getCategory());

        Mockito.verify(this.categoryRepository).findById(holder.getCategoryId());
        Mockito.verify(this.categoryRepository).save(holder.getCategory());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testDeleteCategory(GenerateTestData.TestDataHolder holder) {
        Mockito.when(categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));
        Mockito.when(recipeRepository.findAllByCategory(holder.getCategory())).thenReturn(Collections.emptyList());

        service.delete(holder.getCategoryId());

        Mockito.verify(this.categoryRepository).delete(holder.getCategory());
    }
}
