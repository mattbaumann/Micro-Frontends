package ch.hsr.apparch.purchaselist.controller;

import ch.hsr.apparch.purchaselist.model.PurchaseList;
import ch.hsr.apparch.purchaselist.repository.PurchaseListItemRepository;
import ch.hsr.apparch.purchaselist.repository.PurchaseListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;

@Controller
@RequestMapping("/requests")
public class FormRequests {

    private final PurchaseListRepository purchaseLists;

    private final PurchaseListItemRepository purchaseListItems;

    @Autowired
    public FormRequests(PurchaseListRepository purchaseLists, PurchaseListItemRepository purchaseListItems) {
        this.purchaseLists = purchaseLists;
        this.purchaseListItems = purchaseListItems;
    }

    @PostMapping("/purchaseList/update")
    public String updatePurchaseList(@RequestParam(value = "id", required = false) Long id,
                                     @RequestParam("name") String name,
                                     @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (id == 0) {
            purchaseLists.save(new PurchaseList(name, date, Collections.emptyList()));
        } else {
            purchaseLists.findById(id)
                    .map(purchaseList -> purchaseList.update(name, date))
                    .map(purchaseLists::save)
                    .orElseThrow(ResourceNotFoundException::new);
        }
        return Views.REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @GetMapping("/purchaseList/delete/{id}")
    public String deletePurchaseList(@PathVariable("id") Long id) {
        purchaseLists.deleteById(id);
        return Views.REDIRECT_CONTROLLER_LIST_VIEW;
    }
}
