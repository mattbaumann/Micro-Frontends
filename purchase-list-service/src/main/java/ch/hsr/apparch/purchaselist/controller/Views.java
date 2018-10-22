package ch.hsr.apparch.purchaselist.controller;


import ch.hsr.apparch.purchaselist.model.PurchaseList;
import ch.hsr.apparch.purchaselist.repository.PurchaseListItemRepository;
import ch.hsr.apparch.purchaselist.repository.PurchaseListRepository;
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
    private final PurchaseListRepository purchaseLists;

    private final PurchaseListItemRepository purchaseListItems;

    @Autowired
    public Views(PurchaseListRepository purchaseLists, PurchaseListItemRepository purchaseListItems) {
        this.purchaseLists = purchaseLists;
        this.purchaseListItems = purchaseListItems;
    }

    @GetMapping("/homeView")
    public String home() {
        return "home";
    }

    @GetMapping("/listView")
    public String test(Model model) {
        model.addAttribute("purchaseLists", purchaseLists.findAll());
        return "listFull";
    }

    @GetMapping({"/editList", "/editList/{id}"})
    public String listView(@PathVariable(value = "id", required = false) Long id, Model model) {
        if (id != null)
            model.addAttribute("purchaseList", purchaseLists.findById(id).orElseThrow(ResourceNotFoundException::new));
        else
            model.addAttribute("purchaseList", new PurchaseList());
        return "editFull";
    }

    @GetMapping("/addView")
    public String addPage(Model model) {
        model.addAttribute("purchaseList", new PurchaseList());
        return "editFull";
    }

}
