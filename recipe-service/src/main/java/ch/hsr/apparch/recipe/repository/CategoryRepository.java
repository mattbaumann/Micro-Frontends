package ch.hsr.apparch.recipe.repository;

import ch.hsr.apparch.recipe.model.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {


}
