package ch.hsr.apparch.recipe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BackendIntegrationController {

    private static final String TARGET_SITE_KEY = "targetSite";
    private static final String TARGET_ELEMENT_KEY = "targetElement";

    @GetMapping("/purchaseList/list")
    public String listPurchaseLists(Model model) {
        model.addAttribute(TARGET_SITE_KEY, "http://localhost:9602/purchaseList/list");
        model.addAttribute(TARGET_ELEMENT_KEY, "table");
        return "purchase-list/list";
    }

    @GetMapping("purchaseList/edit")
    public String editPurchaseList(Model model) {
        model.addAttribute(TARGET_SITE_KEY, "http://localhost:9602/purchaseList/edit");
        model.addAttribute(TARGET_ELEMENT_KEY, "form");
        return "purchase-list/list";
    }
}
