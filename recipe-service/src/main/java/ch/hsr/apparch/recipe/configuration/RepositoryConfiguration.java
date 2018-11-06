package ch.hsr.apparch.recipe.configuration;

import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Ingredient;
import ch.hsr.apparch.recipe.model.Instruction;
import ch.hsr.apparch.recipe.model.Recipe;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Category.class, Ingredient.class, Instruction.class, Recipe.class);
    }
}
