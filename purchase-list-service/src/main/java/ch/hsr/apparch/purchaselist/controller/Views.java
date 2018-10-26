package ch.hsr.apparch.purchaselist.controller;


import ch.hsr.apparch.purchaselist.model.PurchaseList;
import ch.hsr.apparch.purchaselist.model.PurchaseListItem;
import ch.hsr.apparch.purchaselist.repository.PurchaseListItemRepository;
import ch.hsr.apparch.purchaselist.repository.PurchaseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@Controller
public class Views {

    private static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/purchaseList/list";
    private final PurchaseListRepository purchaseLists;

    private final PurchaseListItemRepository purchaseListItems;

    @Autowired
    public Views(PurchaseListRepository purchaseLists, PurchaseListItemRepository purchaseListItems) {
        this.purchaseLists = purchaseLists;
        this.purchaseListItems = purchaseListItems;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/purchaseList/list")
    public String listPurchaseLists(Model model) {
        model.addAttribute("purchaseLists", purchaseLists.findAll());
        return "list/list";
    }

    @GetMapping({"/purchaseList/edit", "/purchaseList/edit/{id}"})
    public String listView(@PathVariable(value = "id", required = false) Optional<Long> id, Model model) {
        if (id.isPresent()) {
            PurchaseList toEdit = purchaseLists.findById(
                    id.orElseThrow(ResourceNotFoundException::new)
            ).orElseThrow(ResourceNotFoundException::new);
            model.addAttribute("purchaseList", toEdit);
            model.addAttribute("posturl", "/purchaseList/" + toEdit.getId() + "/update");
        } else {
            model.addAttribute("purchaseList", new PurchaseList());
            model.addAttribute("posturl", "/purchaseList/add");
        }
        return "list/edit";
    }

    @PostMapping("/purchaseList/add")
    public String addPurchaseList(@RequestParam("name") String name,
                                  @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        purchaseLists.save(new PurchaseList(name, date, Collections.emptyList()));
        return Views.REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @PostMapping("/purchaseList/{id}/update")
    public String updatePurchaseList(@PathVariable(value = "id") Long id,
                                     @RequestParam("name") String name,
                                     @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        purchaseLists.findById(id)
                .map(purchaseList -> purchaseList.setName(name).setDate(date))
                .map(purchaseLists::save)
                .orElseThrow(ResourceNotFoundException::new);
        return Views.REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @GetMapping("/purchaseList/{id}/delete")
    public String deletePurchaseList(@PathVariable("id") long id) {
        purchaseLists.deleteById(id);
        return Views.REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @GetMapping("/purchaseList/{plid}/list")
    public String listPurchaseListItems(@PathVariable("plid") long id,
                                        Model model) {
        model.addAttribute("models",
                purchaseLists.findById(id)
                        .map(PurchaseList::getIngredients)
                        .orElseThrow(ResourceNotFoundException::new)
        );
        model.addAttribute("plid", id);
        return "items/list";
    }

    @GetMapping({
            "/purchaseList/{plid}/item/edit",
            "/purchaseList/{plid}/item/{id}/edit"
    })
    public String editPurchaseListItems(@PathVariable("plid") long plid,
                                        @PathVariable(value = "id", required = false) Optional<Long> id,
                                        Model model) {
        model.addAttribute("posturl",
                id.map(i -> "/purchaseList/" + plid + "/item/" + i + "/update")
                        .orElse("/purchaseList/" + plid + "/item/add")
        );
        model.addAttribute("model",
                id.flatMap(purchaseListItems::findById)
                        .orElseGet(PurchaseListItem::new)
        );
        return "items/edit";
    }

    @PostMapping("/purchaseList/{plid}/item/add")
    public String addPurchaseListItem(@PathVariable("plid") long plid,
                                      @RequestParam("name") String name) {
        purchaseLists.findById(plid)
                .map(list -> new PurchaseListItem(name, list))
                .map(purchaseListItems::save)
                .orElseThrow(ResourceNotFoundException::new);
        return "redirect:/purchaseList/" + plid + "/list";
    }

    @PostMapping("/purchaseList/{plid}/item/{id}/update")
    public String updatePurchaseListItem(@PathVariable("plid") long plid,
                                         @PathVariable(value = "id") long id,
                                         @RequestParam("name") String name) {
        purchaseListItems.findById(id)
                .map(item -> item.setName(name))
                .map(purchaseListItems::save)
                .orElseThrow(ResourceNotFoundException::new);
        return "redirect:/purchaseList/" + plid + "/list";
    }

    @GetMapping("/purchaseList/{plid}/item/{id}/remove")
    public String removePurchaseListItem(@PathVariable("plid") long plid,
                                         @PathVariable("id") long id) {
        purchaseListItems.deleteById(id);
        return "redirect:/purchaseList/" + plid + "/list";
    }
}
