package ch.hsr.apparch.kitchenDevices.controller;


import ch.hsr.apparch.kitchenDevices.model.KitchenDevice;
import ch.hsr.apparch.kitchenDevices.repository.KitchenDeviceRepository;
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
public class KitchenDeviceController {

    private static final Logger LOGGER = LogManager.getLogger("Application");

    private static final String BASE_PATH = "/controller/kitchenDevice";
    private static final String REDIRECT_CONTROLLER_LIST_VIEW = "redirect:" + BASE_PATH + "/list";
    private static final String REDIRECT_EXTERN_CONTROLLER = "redirect:http://localhost:{0}/SSFragmentCompositionController/kitchenDevice/list";
    private static final String INTEGRATION_PORT_KEY = "integrationPort";
    private static final String SINGULAR_MODEL_KEY = "model";
    private static final String PLURAL_MODEL_KEY = "models";
    private static final String POSTURL_KEY = "posturl";
    private final KitchenDeviceRepository kitchenDevices;
    private final int port;

    @Autowired
    public KitchenDeviceController(final KitchenDeviceRepository kitchenDevices, @Value("${server.port}") int port) {
        this.kitchenDevices = kitchenDevices;
        this.port = port;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping({"/kitchenDevice/list", "/kitchenDevice/list/{integrationPort}"})
    public String test(
            @PathVariable(value = "integrationPort", required = false) final Optional<Integer> integrationPort,
            final Model model) {
        model.addAttribute(PLURAL_MODEL_KEY, kitchenDevices.findAll());
        model.addAttribute(INTEGRATION_PORT_KEY, integrationPort);
        return "list";
    }

    @GetMapping({"/kitchenDevice/edit", "/kitchenDevice/{id}/edit"})
    public String listView(@PathVariable(value = "id", required = false) final Optional<Long> id,
                           @RequestParam(value = "integrationPort", required = false) final Optional<Integer> IntegrationPort,
                           final Model model) {
        String basePath = IntegrationPort.isPresent() ? "http://localhost:" + port + BASE_PATH : BASE_PATH;
        model.addAttribute(SINGULAR_MODEL_KEY, id.flatMap(kitchenDevices::findById).orElse(new KitchenDevice()));
        model.addAttribute(POSTURL_KEY, id.map(i -> basePath + '/' + i + "/update").orElse(basePath + "/add"));
        IntegrationPort.ifPresent(port -> model.addAttribute(INTEGRATION_PORT_KEY, port));
        return "edit";
    }

    @PostMapping("/kitchenDevice/add")
    public String addKitchenDevice(@RequestParam("name") final String name,
                                   @RequestParam("function") final String function,
                                   @RequestParam(value = "available", required = false) final boolean available,
                                   @RequestParam(value = "integrationPort", required = false) Optional<Integer> integrationPort) {
        kitchenDevices.save(new KitchenDevice(name, function, available));
        return integrationPort.map(port -> MessageFormat.format(REDIRECT_EXTERN_CONTROLLER, port)).orElse(REDIRECT_CONTROLLER_LIST_VIEW);
    }

    @PostMapping("/kitchenDevice/{id}/update")
    public String updateKitchenDevice(@PathVariable(value = "id") final long id,
                                     @RequestParam("name") final String name,
                                     @RequestParam("function") final String function,
                                     @RequestParam(value = "available", required = false) final boolean available,
                                     @RequestParam(value = "integrationPort", required = false) Optional<Integer> integrationPort) {
        kitchenDevices.findById(id)
                .map(kitchenDevice -> kitchenDevice.setName(name).setFunction(function).setAvailable(available))
                .map(kitchenDevices::save)
                .orElseThrow(ResourceNotFoundException::new);
        return integrationPort.map(port -> MessageFormat.format(REDIRECT_EXTERN_CONTROLLER, port)).orElse(REDIRECT_CONTROLLER_LIST_VIEW);
    }

    @GetMapping("/kitchenDevice/{id}/delete")
    public String deleteKitchenDevice(@PathVariable("id") final long id,
                                      @RequestParam(value = "integrationPort", required = false) final Optional<Integer> integrationPort) {
        kitchenDevices.deleteById(id);
        return integrationPort.map(port -> MessageFormat.format(REDIRECT_EXTERN_CONTROLLER, port)).orElse(REDIRECT_CONTROLLER_LIST_VIEW);
    }
}
