package ch.hsr.apparch.recipe.repository;

import ch.hsr.apparch.recipe.model.Instruction;
import ch.hsr.apparch.recipe.model.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.SortedSet;

public interface InstructionRepository extends PagingAndSortingRepository<Instruction, Long> {

    SortedSet<Instruction> findAllByRecipeOrderByPosition(Recipe recipe);
    SortedSet<Instruction> findAllByRecipeIdOrderByPosition(long recipeId);
}
