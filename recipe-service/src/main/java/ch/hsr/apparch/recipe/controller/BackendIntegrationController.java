package ch.hsr.apparch.recipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BackendIntegrationController {

    @GetMapping("/purchaseList/list")
    public String listPurchaseLists() {
        return "purchase-list/listPurchaseList";
    }
}
