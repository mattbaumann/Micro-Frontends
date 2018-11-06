package ch.hsr.apparch.recipe.controller;

import ch.hsr.apparch.recipe.dto.KitchenDevice;
import ch.hsr.apparch.recipe.dto.KitchenDeviceRoot;
import ch.hsr.apparch.recipe.dto.PurchaseList;
import ch.hsr.apparch.recipe.dto.PurchaseListRoot;
import ch.hsr.apparch.recipe.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

@Controller
@RequestMapping("/backend")
public class BackendCompositionController {

    static String KITCHEN_DEVICE_ROOT_PATH = "/kitchenDevices/";
    static String PURCHASE_LIST_ROOT_PATH = "/purchaseLists/";
    WebClient kitchenDeviceService = WebClient.create("http://localhost:9603/api");
    WebClient purchaseListService = WebClient.create("http://localhost:9602/api");

    static String SINGULAR_MODEL_KEY = "model";
    static String PLURAL_MODEL_KEY = "models";
    static String POSTURL_KEY = "posturl";

    @GetMapping("/kitchenDevice/list")
    public @NotNull Mono<ModelAndView> listKitchenDevice() {
        return kitchenDeviceService.get()
                .uri(KITCHEN_DEVICE_ROOT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(KitchenDeviceRoot.class)
                .map(kitchenDeviceRoot ->
                        new ModelAndView("kitchenDevice/list").addObject(PLURAL_MODEL_KEY, kitchenDeviceRoot)
                );
    }

    @GetMapping("purchaseList/list")
    public @NotNull Mono<ModelAndView> listPurchaseList() {
        return purchaseListService.get()
                .uri(PURCHASE_LIST_ROOT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(PurchaseListRoot.class)
                .map(purchaseListRoot -> {
                    LOGGER.info("Loaded purchase lists {}", purchaseListRoot);
                    return new ModelAndView("purchaseList/list")
                            .addObject(PLURAL_MODEL_KEY, purchaseListRoot);
                });
    }

    @GetMapping({"/purchaseList/edit", "/purchaseList/{id}/edit"})
    public @NotNull Mono<ModelAndView> editPurchaseList(@PathVariable("id") Optional<Long> id) {
        return id.map(i -> purchaseListService.get()
                .uri(PURCHASE_LIST_ROOT_PATH + id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(PurchaseList.class)
                .map(purchaseList ->
                        new ModelAndView("purchaseList/edit")
                                .addObject(SINGULAR_MODEL_KEY, purchaseList)
                                .addObject(POSTURL_KEY, "/backend/kitchenDevice/" + i + "/update")
                )
        ).orElseGet(() -> Mono.just(new ModelAndView("purchaseList/edit")
                .addObject(SINGULAR_MODEL_KEY, new PurchaseList())
                .addObject(POSTURL_KEY, "/backend/kitchenDevice/add"))
        );
    }

    @GetMapping({"/kitchenDevice/edit", "/kitchenDevice/{id}/edit"})
    public @NotNull Mono<ModelAndView> editKitchenDevice(
            @NotNull @PathVariable(value = "id", required = false) Optional<Long> id) {
        return id.map(i -> kitchenDeviceService.get()
                .uri(KITCHEN_DEVICE_ROOT_PATH + i)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve().bodyToMono(KitchenDevice.class)
                .map(kitchenDevice -> new ModelAndView("kitchenDevice/edit")
                        .addObject(SINGULAR_MODEL_KEY, kitchenDevice)
                        .addObject(POSTURL_KEY, "/backend/kitchenDevice/" + i + "/update")
                )
        ).orElseGet(() -> Mono.just(new ModelAndView("kitchenDevice/edit")
                        .addObject(SINGULAR_MODEL_KEY, new KitchenDevice())
                        .addObject(POSTURL_KEY, "/backend/kitchenDevice/add")
                )
        );
    }

    @PostMapping("/purchaseList/{id}/update")
    public @NotNull Mono<String> updatePurchaseList(
            @PathVariable("id") long id,
            @NotNull @RequestBody PurchaseList purchaseList) {

        return purchaseListService.put()
                .uri(PURCHASE_LIST_ROOT_PATH + id)
                .body(BodyInserters.fromObject(purchaseList))
                .exchange()
                .map(response -> testResponseAndReturnRedirect(id, "redirect:/backend/purchaseList/list", response));
    }

    @PostMapping("purchaseList/add")
    public @NotNull Mono<String> addPurchaseList(PurchaseList list) {
        return purchaseListService.put()
                .uri(PURCHASE_LIST_ROOT_PATH)
                .body(BodyInserters.fromObject(list))
                .exchange()
                .map(response -> testResponseAndReturnRedirect(list.getId(), "redirect:/backend/purchaseList/list", response));
    }

    @PostMapping("/kitchenDevice/{id}/update")
    public @NotNull Mono<String> updateKitchenDevice(
            @PathVariable("id") long id,
            @NotNull @RequestBody MultiValueMap<String, String> values) {
        return kitchenDeviceService.put()
                .uri(KITCHEN_DEVICE_ROOT_PATH + id)
                .body(BodyInserters.fromObject(
                        new KitchenDevice(0,
                                values.getFirst("name"),
                                values.getFirst("function"),
                                values.containsKey("available")
                        )
                ))
                .exchange()
                .map(response -> testResponseAndReturnRedirect(id, "redirect:/backend/kitchenDevice/list", response));
    }

    @PostMapping("/kitchenDevice/add")
    public @NotNull Mono<String> addKitchenDevice(@NotNull @RequestBody MultiValueMap<String, String> valueMap) {
        return kitchenDeviceService.put()
                .uri(KITCHEN_DEVICE_ROOT_PATH)
                .body(BodyInserters.fromObject(
                        new KitchenDevice(0,
                                valueMap.getFirst("name"),
                                valueMap.getFirst("function"),
                                valueMap.containsKey("available")
                        )
                ))
                .exchange()
                .map(response -> testResponseAndReturnRedirect(0, "redirect:/backend/kitchenDevice/list", response));
    }

    @GetMapping("/kitchenDevice/{id}/delete")
    public @NotNull Mono<String> deleteKitchenDevice(@PathVariable("id") long id) {
        return kitchenDeviceService.get()
                .uri(KITCHEN_DEVICE_ROOT_PATH + id)
                .exchange()
                .map(response -> testResponseAndReturnRedirect(id, "redirect:/backend/kitchenDevice/list", response));
    }

    private static @NotNull String testResponseAndReturnRedirect(@NotNull long id, @NotNull String redirect, ClientResponse clientResponse) {
        if (clientResponse.statusCode() != HttpStatus.NOT_FOUND) return redirect;
        else throw ResourceNotFoundException.withRecordNotFoundMessage(KitchenDevice.class, id).get();
    }
}
