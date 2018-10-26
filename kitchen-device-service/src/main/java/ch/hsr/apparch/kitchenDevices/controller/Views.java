package ch.hsr.apparch.kitchenDevices.controller;


import ch.hsr.apparch.kitchenDevices.model.KitchenDevice;
import ch.hsr.apparch.kitchenDevices.repository.KitchenDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping()
public class Views {

    private static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:/kitchenDevice/list";
    private final KitchenDeviceRepository kitchenDevices;

    @Autowired
    public Views(final KitchenDeviceRepository kitchenDevices) {
        this.kitchenDevices = kitchenDevices;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/kitchenDevice/list")
    public String test(final Model model) {
        model.addAttribute("models", kitchenDevices.findAll());
        return "list";
    }

    @GetMapping({"/kitchenDevice/edit", "/kitchenDevice/{id}/edit"})
    public String listView(@PathVariable(value = "id", required = false) final Optional<Long> id, final Model model) {
        model.addAttribute("model", id.flatMap(kitchenDevices::findById).orElse(new KitchenDevice()));
        model.addAttribute("posturl", id.map(i -> "/kitchenDevice/" + i + "/update").orElse("/kitchenDevice/add"));
        return "edit";
    }

    @PostMapping("/kitchenDevice/add")
    public String addKitchenDevice(@RequestParam("name") final String name,
                                   @RequestParam("function") final String function,
                                   @RequestParam(value = "available", required = false) final boolean available) {
        kitchenDevices.save(new KitchenDevice(name, function, available));
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @PostMapping("/kitchenDevice/{id}/update")
    public String updatePurchaseList(@PathVariable(value = "id") final long id,
                                     @RequestParam("name") final String name,
                                     @RequestParam("function") final String function,
                                     @RequestParam(value = "available", required = false) final boolean available) {
        kitchenDevices.findById(id)
                .map(kitchenDevice -> kitchenDevice.setName(name).setFunction(function).setAvailable(available))
                .map(kitchenDevices::save)
                .orElseThrow(ResourceNotFoundException::new);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }

    @GetMapping("/kitchenDevice/{id}/delete")
    public String deleteKitchenDevice(@PathVariable("id") final long id) {
        kitchenDevices.deleteById(id);
        return REDIRECT_CONTROLLER_LIST_VIEW;
    }
}
