package ch.hsr.apparch.recipe.controller;

import ch.hsr.apparch.recipe.model.Recipe;
import ch.hsr.apparch.recipe.repository.CategoryRepository;
import ch.hsr.apparch.recipe.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
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

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/list")
    public String list(final Model model) {
        model.addAttribute("models", recipeRepository.findAll());
        return "listRecipe";
    }

    @GetMapping({"/edit", "/edit/{id}"})
    public String edit(
            @PathVariable(value = "id", required = false) final Long id,
            final Model model) {
        if (id != null)
            model.addAttribute("model", recipeRepository.findById(id));
        else
            model.addAttribute("model", new Recipe());
        model.addAttribute("categories", categoryRepository.findAll());
        return "editRecipe";
    }

    @PostMapping("/update")
    public String update(@RequestParam(value = "id", required = false) final Long id,
                         @RequestParam("name") final String name,
                         @RequestParam("category") final int category_id) {
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }
}
