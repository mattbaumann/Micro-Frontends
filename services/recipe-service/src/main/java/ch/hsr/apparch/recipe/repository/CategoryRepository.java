package ch.hsr.apparch.recipe.repository;

import ch.hsr.apparch.recipe.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

    Optional<Category> findByName(final String name);
}
