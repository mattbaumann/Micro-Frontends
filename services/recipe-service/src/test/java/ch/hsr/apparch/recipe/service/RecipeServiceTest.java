package ch.hsr.apparch.recipe.service;

import ch.hsr.apparch.recipe.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import lombok.AccessLevel;
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

import javax.persistence.GeneratedValue;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE)
@Tag("service")
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class RecipeServiceTest {


    @MockBean
    RecipeRepository recipeRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    RecipeService service;
    
    private static Stream<GenerateTestData.TestDataHolder> createTestData() {
        return GenerateTestData.createTestData();
    }
    
    @BeforeEach
    @SuppressWarnings("unchecked")
    void setup() {
        Mockito.clearInvocations(recipeRepository, categoryRepository);
    }

    @Test
    void testRecipesDI() {
        assertThat(recipeRepository).isNotNull();
    }

    @Test
    void testCategoriesDI() {
        assertThat(categoryRepository).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testListRecipes(GenerateTestData.TestDataHolder holder) {
        Mockito.when(recipeRepository.findAll(Sort.by(Sort.Order.asc(Recipe.NAME_PROPERTY)))).thenReturn(Collections.singletonList(holder.getRecipe()));

        assertThat(service.listRecipes()).containsExactly(holder.getRecipe());

        Mockito.verify(recipeRepository).findAll(Sort.by(Sort.Order.asc(Recipe.NAME_PROPERTY)));
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testListRecipesByCategory(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.recipeRepository.findAllByCategory(holder.getCategory()))
                .thenReturn(holder.getCategory().getRecipes());
        Mockito.when(categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));

        assertThat(service.listRecipesByCategory(holder.getCategoryId()))
                .contains(holder.getRecipe()).isEqualTo(holder.getCategory().getRecipes());

        Mockito.verify(this.recipeRepository).findAllByCategory(holder.getCategory());
        Mockito.verify(categoryRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testListRecipesByUnknownCategory(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.recipeRepository.findAllByCategory(holder.getCategory())).thenReturn(holder.getCategory().getRecipes());
        Mockito.when(categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.listRecipesByCategory(holder.getCategoryId())).isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(this.recipeRepository, Mockito.never()).findAllByCategory(holder.getCategory());
        Mockito.verify(categoryRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testCategoryNameById(GenerateTestData.TestDataHolder holder) {
        Mockito.when(categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));

        assertThat(service.getCategoryNameById(holder.getCategoryId())).isEqualTo(holder.getCategory().getName());

        Mockito.verify(categoryRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testCategoryNameByUnknownId(GenerateTestData.TestDataHolder holder) {
        Mockito.when(categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCategoryNameById(holder.getCategoryId())).isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(categoryRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testFindKnownRecipeOrNew(GenerateTestData.TestDataHolder holder) {
        Mockito.when(recipeRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getRecipe()));

        assertThat(service.findRecipeOrNew(Optional.of(holder.getCategoryId()))).isEqualTo(holder.getRecipe());

        Mockito.verify(recipeRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testFindUnknownRecipeOrNew(GenerateTestData.TestDataHolder holder) {
        Mockito.when(recipeRepository.findById(holder.getCategoryId())).thenReturn(Optional.empty());

        assertThat(service.findRecipeOrNew(Optional.of(holder.getCategoryId())))
                .isEqualTo(new Recipe());

        Mockito.verify(recipeRepository).findById(holder.getCategoryId());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testAddRecipe(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));
        Mockito.when(this.recipeRepository.save(holder.getRecipe())).thenReturn(holder.getRecipe());

        assertThat(service.add(holder.getName(), holder.getCategoryId())).isEqualTo(holder.getRecipe());

        Mockito.verify(this.recipeRepository).save(holder.getRecipe());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testUpdateUnknownRecipe(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));
        Mockito.when(this.recipeRepository.findById(holder.getRecipeId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(holder.getRecipeId(), holder.getName(), holder.getCategoryId()))
                .isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(this.categoryRepository).findById(holder.getCategoryId());
        Mockito.verify(this.recipeRepository).findById(holder.getRecipeId());
        Mockito.verify(this.recipeRepository, Mockito.never()).save(holder.getRecipe());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testUpdateRecipeWithUnknownCategory(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.categoryRepository.findById(holder.getRecipeId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(holder.getRecipeId(), holder.getName(), holder.getCategoryId()))
                .isInstanceOf(ResourceNotFoundException.class);

        Mockito.verify(this.categoryRepository).findById(holder.getCategoryId());
        Mockito.verify(this.recipeRepository, Mockito.never()).findById(holder.getRecipeId());
        Mockito.verify(this.recipeRepository, Mockito.never()).save(holder.getRecipe());
    }

    @ParameterizedTest
    @MethodSource("createTestData")
    void testUpdateRecipe(GenerateTestData.TestDataHolder holder) {
        Mockito.when(this.categoryRepository.findById(holder.getCategoryId())).thenReturn(Optional.of(holder.getCategory()));
        Mockito.when(this.recipeRepository.findById(holder.getRecipeId())).thenReturn(Optional.of(holder.getRecipe()));
        Mockito.when(this.recipeRepository.save(holder.getRecipe())).thenReturn(holder.getRecipe());

        assertThat(service.update(holder.getRecipeId(), holder.getName(), holder.getCategoryId())).isEqualTo(holder.getRecipe());

        Mockito.verify(this.categoryRepository).findById(holder.getCategoryId());
        Mockito.verify(this.recipeRepository).findById(holder.getRecipeId());
        Mockito.verify(this.recipeRepository).save(holder.getRecipe());
    }

    @Test
    void testDeleteRecipe() {
        final long id = 100;

        service.delete(id);

        Mockito.verify(this.recipeRepository).deleteById(id);
    }
}
