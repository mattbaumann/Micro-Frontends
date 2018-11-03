package ch.hsr.apparch.recipe.service;

import ch.hsr.apparch.recipe.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Instruction;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.IngredientRepository;
import ch.hsr.apparch.recipe.repository.InstructionRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final InstructionRepository instructionRepository;
    private final IngredientRepository ingredientRepository;

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

    public Collection<Instruction> listInstructionByRecipeId(int recipeId) {
        return instructionRepository.findAllByRecipeIdOrderByPosition(recipeId);
    }

    @Transactional
    public Instruction addInstruction(final long recipeId, String name, long position) {
        return recipeRepository.findById(recipeId)
                .map(recipe -> new Instruction(name, recipe,
                        getLastInstructionPosition(recipe))
                )
                .map(instructionRepository::save)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Recipe.class, recipeId));
    }

    private long getLastInstructionPosition(Recipe recipe) {
        return instructionRepository.findAllByRecipeOrderByPosition(recipe).last().getPosition() + 1;
    }

    @Transactional
    public Instruction updateInstruction(final long instructionId, String description) {
        return instructionRepository.findById(instructionId)
                .map(instruction -> instruction.setDescription(description))
                .map(instructionRepository::save)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Recipe.class, instructionId));
    }

    public void deleteInstruction(final long instructionId) {
        instructionRepository.deleteById(instructionId);
    }
}
