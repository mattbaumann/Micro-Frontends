package ch.hsr.apparch.recipe.controller;

import ch.hsr.apparch.recipe.model.Category;
import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    private static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/controller/listView";

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public RecipeController(final RecipeRepository recipeRepository,
                            final CategoryRepository categoryRepository) {
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/list")
    public String list(final Model model) {
        model.addAttribute("models", recipeRepository.findAll());
        return "recipe/list";
    }

    @GetMapping({"/edit", "/{id}/edit"})
    public String edit(@PathVariable(value = "id", required = false) final Optional<Long> id,
                       final Model model) {
        model.addAttribute("model", id.flatMap(recipeRepository::findById).orElse(new Recipe()));
        model.addAttribute("categories", categoryRepository.findAll());
        return "recipe/edit";
    }

    @PostMapping("{id}/update")
    public String update(@PathVariable("id") final long id,
                         @RequestParam("name") final String name,
                         @RequestParam("category") final long categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(ResourceNotFoundException::new);
        recipeRepository.findById(id)
                .map(recipe -> recipe.setName(name).setCategory(category))
                .map(recipeRepository::save);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @PostMapping("/add")
    public String update(@RequestParam("name") final String name,
                         @RequestParam("category") final long category_id) {

        categoryRepository.findById(category_id)
                .map(category -> new Recipe(name, Collections.emptyList(), Collections.emptyList(), category))
                .map(recipeRepository::save)
                .orElseThrow(ResourceNotFoundException::new);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") final long id) {
        recipeRepository.deleteById(id);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }
}
