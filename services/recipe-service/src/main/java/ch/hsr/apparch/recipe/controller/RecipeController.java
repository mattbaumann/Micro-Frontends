package ch.hsr.apparch.recipe.controller;

import ch.hsr.apparch.recipe.service.CategoryService;
import ch.hsr.apparch.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Optional;

@Controller
@RequestMapping("/controller/recipe")
@RequiredArgsConstructor
public class RecipeController {

    private static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/controller/recipe/list";
    private static final String SINGULAR_MODEL_KEY = "model";
    private static final String PLURAL_MODEL_KEY = "models";
    private static final String POSTURL_KEY = "posturl";
    private static final String CATEGORIES_KEY = "categories";
    private static final String LISTING_TITLE = "listingTitle";
    private static final String GLOBAL_LISTING_TITLE = "global listing of all recipes";
    private static final String CATEGORY_LISTING_TITLE = "category ''{0}'' listing of all recipes";

    private final RecipeService recipeService;
    private final CategoryService categoryService;

    @GetMapping("/list")
    public String list(final Model model) {
        model.addAttribute(PLURAL_MODEL_KEY, recipeService.listRecipes());
        model.addAttribute(LISTING_TITLE, GLOBAL_LISTING_TITLE);
        return "recipe/list";
    }

    @GetMapping("/list/category/{id}/list")
    public String listByCategory(@PathVariable("id") final long id, final Model model) {
        model.addAttribute(PLURAL_MODEL_KEY, recipeService.listRecipesByCategory(id));
        model.addAttribute(LISTING_TITLE,
                MessageFormat.format(CATEGORY_LISTING_TITLE, recipeService.getCategoryNameById(id))
        );
        return "recipe/list";
    }

    @GetMapping({"/edit", "/{id}/edit"})
    public String edit(@PathVariable(value = "id", required = false) final Optional<Long> id, final Model model) {
        model.addAttribute(SINGULAR_MODEL_KEY, recipeService.findRecipeOrNew(id));
        model.addAttribute(CATEGORIES_KEY, categoryService.listCategories());
        model.addAttribute(POSTURL_KEY, id.map(i -> "/controller/recipe/" + i + "/update").orElse("/controller/recipe/add"));
        return "recipe/edit";
    }

    @PostMapping("{id}/update")
    public String update(@PathVariable("id") final long id,
                         @RequestParam("name") final String name,
                         @RequestParam("category") final long categoryId) {
        recipeService.update(id, name, categoryId);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @PostMapping("/add")
    public String add(@RequestParam("name") final String name,
                         @RequestParam("category") final long category_id) {
        recipeService.add(name, category_id);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") final long id) {
        recipeService.delete(id);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }
}
