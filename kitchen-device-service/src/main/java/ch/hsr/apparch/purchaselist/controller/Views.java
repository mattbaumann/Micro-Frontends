package ch.hsr.apparch.purchaselist.controller;


import ch.hsr.apparch.purchaselist.model.KitchenDevice;
import ch.hsr.apparch.purchaselist.repository.KitchenDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/controller")
public class Views {

    static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/controller/listView";
    private final KitchenDeviceRepository purchaseLists;

    @Autowired
    public Views(KitchenDeviceRepository purchaseLists) {
        this.purchaseLists = purchaseLists;
    }

    @GetMapping("/homeView")
    public String home() {
        return "home";
    }

    @GetMapping("/listView")
    public String test(Model model) {
        model.addAttribute("models", purchaseLists.findAll());
        return "list";
    }

    @GetMapping({"/editList", "/editList/{id}"})
    public String listView(@PathVariable(value = "id", required = false) Long id, Model model) {
        if (id != null)
            model.addAttribute("model", purchaseLists.findById(id).orElseThrow(ResourceNotFoundException::new));
        else
            model.addAttribute("model", new KitchenDevice());
        return "edit";
    }

    @GetMapping("/addView")
    public String addPage(Model model) {
        model.addAttribute("model", new KitchenDevice());
        return "edit";
    }

}
