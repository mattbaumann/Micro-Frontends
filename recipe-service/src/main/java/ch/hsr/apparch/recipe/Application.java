package ch.hsr.apparch.recipe;

import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner insertSampleData(RecipeRepository recipeRepository, CategoryRepository categoryRepository) {
        return args -> {
            Category sample_category = categoryRepository.save(new Category("Sample Category", Collections.emptyList()));
            Recipe sample_recipe1 = recipeRepository.save(new Recipe("Sample Recipe 1", Collections.emptyList(), Collections.emptyList(), sample_category));
            Recipe sample_recipe2 = recipeRepository.save(new Recipe("Sample Recipe 2", Collections.emptyList(), Collections.emptyList(), sample_category));
            Recipe sample_recipe3 = recipeRepository.save(new Recipe("Sample Recipe 3", Collections.emptyList(), Collections.emptyList(), sample_category));
        };
    }
}
