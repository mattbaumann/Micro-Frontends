package ch.hsr.apparch.recipe.repository;

import ch.hsr.apparch.recipe.model.Ingredient;
import ch.hsr.apparch.recipe.model.Instruction;
import ch.hsr.apparch.recipe.model.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;

@CrossOrigin
public interface IngredientRepository extends PagingAndSortingRepository<Ingredient, Long> {

    Collection<Ingredient> findAllByRecipe(Recipe recipe);
    Collection<Ingredient> findAllByRecipeId(long recipeId);
}
