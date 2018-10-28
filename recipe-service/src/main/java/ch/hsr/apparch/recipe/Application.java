package ch.hsr.apparch.recipe;

import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class Application {

    private static final Logger LOGGER = LogManager.getLogger("Application");

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

    @Bean
    public CommandLineRunner logPortAtStartup(@Value("${server.port}") int port) {
        return args -> LOGGER.info("Spring Server 'Kitchen Devices' is running under port {}", port);
    }
}
