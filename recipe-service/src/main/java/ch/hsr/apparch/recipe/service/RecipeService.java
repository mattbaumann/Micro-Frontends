package ch.hsr.apparch.recipe.service;

import ch.hsr.apparch.recipe.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public RecipeService(RecipeRepository recipeRepository, CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Recipe> listRecipes() {
        return recipeRepository.findAll(Sort.by(Sort.Order.asc(Recipe.NAME_PROPERTY)));
    }

    @Transactional
    public Iterable<Recipe> listRecipesByCategory(final long id) {
        return recipeRepository.findAllByCategory(
                categoryRepository.findById(id)
                        .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Category.class, id))
        );
    }

    public String getCategoryNameById(final long id) {
        return categoryRepository.findById(id).map(Category::getName)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Category.class, id));
    }

    public Recipe findRecipeOrNew(Optional<Long> id) {
        return id.flatMap(recipeRepository::findById).orElseGet(Recipe::new);
    }

    @Transactional
    public Recipe update(final long id, String name, final long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Category.class, id));
        return recipeRepository.findById(id)
                .map(recipe -> recipe.setName(name).setCategory(category))
                .map(recipeRepository::save)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Recipe.class, id));
    }

    @Transactional
    public Recipe add(String name, final long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> new Recipe(name, Collections.emptyList(), Collections.emptyList(), category))
                .map(recipeRepository::save)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Category.class, categoryId));
    }

    public void delete(final long id) {
        recipeRepository.deleteById(id);
    }
}
