package ch.hsr.apparch.recipe.service;

import ch.hsr.apparch.recipe.exceptions.DuplicateResourceException;
import ch.hsr.apparch.recipe.exceptions.NotEmptyCategoryException;
import ch.hsr.apparch.recipe.exceptions.ResourceNotFoundException;
import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Category> listCategories() {
        return categoryRepository.findAll(Sort.by(Sort.Order.asc(Category.NAME_PROPERTY)));
    }

    @Transactional
    public Category findOrNew(Optional<Long> id) {
        return id.flatMap(categoryRepository::findById).orElse(new Category());
    }

    @Transactional
    public Category update(final long id, String description) {
        categoryRepository.findByName(description).ifPresent(category -> {
            throw NotEmptyCategoryException.withCategoryNotEmptyMessage(category);
        });
        return categoryRepository.findById(id)
                .map(category -> category.setName(description))
                .map(categoryRepository::save)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Category.class, id));
    }

    @Transactional
    public Category add(String description) {
        categoryRepository.findByName(description).ifPresent(category -> {
            throw DuplicateResourceException.withDuplicateMessage(Category.class, category.getName());
        });
        return categoryRepository.save(new Category(description, Collections.emptyList()));
    }

    @Transactional
    public void delete(final long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(ResourceNotFoundException.withRecordNotFoundMessage(Category.class, id));
        if (!category.getRecipes().isEmpty()) {
            throw NotEmptyCategoryException.withCategoryNotEmptyMessage(category);
        }
        categoryRepository.delete(category);
    }
}
