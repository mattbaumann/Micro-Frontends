package ch.hsr.apparch.recipe.controller;

import ch.hsr.apparch.recipe.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/controller/category")
public class CategoryController {

    private static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/controller/category/list";
    private static final String SINGULAR_MODEL_KEY = "model";
    private static final String PLURAL_MODEL_KEY = "models";
    private static final String POSTURL_KEY = "posturl";

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String list(final Model model) {
        model.addAttribute(PLURAL_MODEL_KEY, categoryService.listCategories());
        return "category/list";
    }

    @GetMapping({"/edit", "/{id}/edit"})
    public String edit(@PathVariable(value = "id", required = false) final Optional<Long> id, final Model model) {
        model.addAttribute(SINGULAR_MODEL_KEY, categoryService.findOrNew(id));
        model.addAttribute(POSTURL_KEY, id.map(i -> "/controller/category/" + i + "/update").orElse("/controller/category/add"));
        return "category/edit";
    }

    @PostMapping("{id}/update")
    public String update(@PathVariable("id") final long id, @RequestParam("name") final String name) {
        categoryService.update(id, name);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @PostMapping("/add")
    public String add(@RequestParam("name") final String name) {
        categoryService.add(name);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") final long id) {
        categoryService.delete(id);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }
}
