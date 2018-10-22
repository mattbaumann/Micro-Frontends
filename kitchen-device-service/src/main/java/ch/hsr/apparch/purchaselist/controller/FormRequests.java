package ch.hsr.apparch.purchaselist.controller;

import ch.hsr.apparch.purchaselist.model.KitchenDevice;
import ch.hsr.apparch.purchaselist.repository.KitchenDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/requests")
public class FormRequests {

    private final KitchenDeviceRepository kitchenDevices;

    @Autowired
    public FormRequests(KitchenDeviceRepository kitchenDevices) {
        this.kitchenDevices = kitchenDevices;
    }

    @PostMapping("/purchaseList/update")
    public String updatePurchaseList(@RequestParam(value = "id", required = false) Long id,
                                     @RequestParam("name") String name,
                                     @RequestParam("function") String function,
                                     @RequestParam(value = "available", required = false) boolean available) {
        if (id == 0) {
            kitchenDevices.save(new KitchenDevice(name, function, available));
        } else {
            kitchenDevices.findById(id)
                    .map(kitchenDevice -> kitchenDevice.update(name, function, available))
                    .map(kitchenDevices::save)
                    .orElseThrow(ResourceNotFoundException::new);
        }
        return Views.REDIRECT_CONTROLLER_LIST_VIEW;
    }
}
