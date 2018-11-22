package ch.hsr.apparch.recipe.service;

import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import lombok.AccessLevel;
import lombok.Value;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class GenerateTestData {

    Random random = new Random();

    static Stream<TestDataHolder> createTestData() {
        long categoryId = random.nextInt(Integer.MAX_VALUE);
        String categoryName = "Test";
        Category category = new Category(categoryName, Collections.emptyList());
        String name = "a test";
        long recipeId = random.nextInt(Integer.MAX_VALUE);
        Recipe recipe = new Recipe(name, Collections.emptyList(), Collections.emptyList(), category);
        List<Recipe> recipes = Collections.singletonList(recipe);
        category.setRecipes(recipes);
        return Stream.of(new TestDataHolder(categoryId, recipeId, category, recipe, name));
    }

    @Value
    static class TestDataHolder {
        long categoryId;
        long recipeId;
        Category category;
        Recipe recipe;
        String name;
    }
}
