package ch.hsr.apparch.recipe.repository;

import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;

@CrossOrigin
public interface RecipeRepository extends PagingAndSortingRepository<Recipe, Long> {

    Collection<Recipe> findAllByCategory(Category category);

}

