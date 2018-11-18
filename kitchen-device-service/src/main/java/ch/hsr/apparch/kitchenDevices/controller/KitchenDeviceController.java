package ch.hsr.apparch.kitchenDevices.controller;


import ch.hsr.apparch.kitchenDevices.model.KitchenDevice;
import ch.hsr.apparch.kitchenDevices.repository.KitchenDeviceRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.Optional;

@Controller
@RequestMapping("/controller")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class KitchenDeviceController {

    static String BASE_PATH = "/controller/kitchenDevice";
    static String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:" + BASE_PATH + "/list";
    static String SINGULAR_MODEL_KEY = "model";
    static String PLURAL_MODEL_KEY = "models";
    static String POSTURL_KEY = "posturl";
    
    KitchenDeviceRepository kitchenDevices;
    
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/kitchenDevice/list")
    public String listKitchenDevices(
            final Model model) {
        model.addAttribute(PLURAL_MODEL_KEY, kitchenDevices.findAll());
        return "list";
    }

    @GetMapping({"/kitchenDevice/edit", "/kitchenDevice/{id}/edit"})
    public String editKitchenDevice(@PathVariable(value = "id", required = false) Optional<Long> id,
                           final Model model) {
        model.addAttribute(SINGULAR_MODEL_KEY, id.flatMap(kitchenDevices::findById).orElse(new KitchenDevice()));
        model.addAttribute(POSTURL_KEY, id.map(i -> BASE_PATH + '/' + i + "/update").orElse(BASE_PATH + "/add"));
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
    public String updateKitchenDevice(@PathVariable(value = "id") final long id,
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
