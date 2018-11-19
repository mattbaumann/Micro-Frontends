package ch.hsr.apparch.recipe.repository;

import ch.hsr.apparch.recipe.model.Instruction;
import ch.hsr.apparch.recipe.model.Recipe;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Collection;
import java.util.SortedSet;

@CrossOrigin
public interface InstructionRepository extends PagingAndSortingRepository<Instruction, Long> {

    SortedSet<Instruction> findAllByRecipeOrderByPosition(Recipe recipe);
    SortedSet<Instruction> findAllByRecipeIdOrderByPosition(long recipeId);
}
