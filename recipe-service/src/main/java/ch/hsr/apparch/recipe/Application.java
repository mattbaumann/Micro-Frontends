package ch.hsr.apparch.recipe;

import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

/**
 * Main class of the application.
 * <p>
 * While it uses the startup process of <em>Spring Boot</em> it defines it's own additions to it. The Function {@link #insertSampleData(RecipeRepository, CategoryRepository)}
 * inserts sample data into the database before running the application. And {@link #logPortAtStartup(int)} logs the port after loading the appliation container.
 */
@Log4j2
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

    @Bean
    public CommandLineRunner logPortAtStartup(@Value("${server.port}") int port) {
        return args -> LOGGER.info("Spring Server 'Kitchen Devices' is running under port {}", port);
    }
}
