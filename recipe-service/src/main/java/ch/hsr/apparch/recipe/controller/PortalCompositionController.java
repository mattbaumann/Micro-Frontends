package ch.hsr.apparch.recipe.controller;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.HandlerMapping;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Log4j2
@Controller
@RequestMapping("/controller")
public class PortalCompositionController {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private static final String TARGET_SITE_KEY = "targetSite";
    private static final String TARGET_ELEMENT_KEY = "targetElement";

    private final int port;

    @Autowired
    public PortalCompositionController(@Value("${server.port}") int port) {
        this.port = port;
    }

    @GetMapping("/kitchenDevice/**")
    public Mono<String> kitchenDeviceService(@NotNull Model model, @NotNull HttpServletRequest request) {
        return matchPath(request).map(path -> {
            LOGGER.info("Path {} was requested", path);
            model.addAttribute(TARGET_SITE_KEY, "http://localhost:9603/controller/kitchenDevice/" + path);
            model.addAttribute(TARGET_ELEMENT_KEY, path.endsWith("edit") ? "form" : "table");
            return "portal/frameHolder";
        });
    }

    @PostMapping("/kitchenDevice/add")
    public Mono<String> addKitchenDeviceService(@NotNull Model model,
                                                @RequestBody MultiValueMap<String, String> device,
                                                @NotNull HttpServletRequest request) {
        return Mono.just(request).map(r -> "http://localhost:9603/controller/kitchenDevice/add")
                .flatMap(url -> WebClient.create().post().uri(url).body(BodyInserters.fromFormData(device)).exchange())
                .map(httpClientResponse -> {
                    LOGGER.fatal(httpClientResponse.statusCode());
                    return "redirect:/controller/kitchenDevice/list";
                });
    }

    @PostMapping("/kitchenDevice/{id}/update")
    public Mono<String> postKitchenDeviceService(@NotNull Model model,
                                                 @PathVariable("id") long id,
                                                 @RequestBody MultiValueMap<String, String> device,
                                                 @NotNull HttpServletRequest request) {
        return matchPath(request).map(path -> "http://localhost:9603/controller/kitchenDevice/" + id + "/update")
                .flatMap(url -> WebClient.create().post().uri(url).body(BodyInserters.fromFormData(device)).exchange())
                .map(httpClientResponse -> {
                    LOGGER.fatal(httpClientResponse.statusCode());
                    return "redirect:/controller/kitchenDevice/list";
                });
    }

    @GetMapping("/purchaseList/**")
    public Mono<String> purchaseListService(@NotNull Model model, @NotNull HttpServletRequest request) {
        return matchPath(request).map(path -> {
            LOGGER.info("Path {} was requested", path);
            model.addAttribute(TARGET_SITE_KEY, "http://localhost:9602/controller/purchaseList/" + path);
            model.addAttribute(TARGET_ELEMENT_KEY, path.endsWith("edit") ? "form" : "table");
            return "portal/frameHolder";
        });
    }

    @PostMapping("/purchaseList/add")
    public Mono<String> addPurchaseListService(@RequestBody MultiValueMap<String, String> device,
                                               @NotNull HttpServletRequest request) {
        return Mono.just(request).map(r -> "http://localhost:9602/controller/purchaseList/add")
                .flatMap(url -> WebClient.create().post().uri(url).body(BodyInserters.fromFormData(device)).exchange())
                .map(httpClientResponse -> {
                    LOGGER.fatal(httpClientResponse.statusCode());
                    return "redirect:/controller/purchaseList/list";
                });
    }

    @PostMapping("/purchaseList/{id}/update")
    public Mono<String> postPurchaseListService(@PathVariable("id") long id,
                                                @RequestBody MultiValueMap<String, String> device,
                                                @NotNull HttpServletRequest request) {
        return matchPath(request).map(path -> "http://localhost:9602/controller/purchaseList/" + id + "/update")
                .flatMap(url -> WebClient.create().post().uri(url).body(BodyInserters.fromFormData(device)).exchange())
                .map(httpClientResponse -> {
                    LOGGER.fatal(httpClientResponse.statusCode());
                    return "redirect:/controller/purchaseList/list";
                });
    }

    @PostMapping("/purchaseList/{id}/item/add")
    public Mono<String> addPurchaseListItemService(@PathVariable("id") long id,
                                                   @RequestBody MultiValueMap<String, String> device,
                                                   @NotNull HttpServletRequest request) {
        return Mono.just(request).map(r -> "http://localhost:9602/controller/purchaseList/" + id + "/item/add")
                .flatMap(url -> WebClient.create().post().uri(url).body(BodyInserters.fromFormData(device)).exchange())
                .map(httpClientResponse -> {
                    LOGGER.fatal(httpClientResponse.statusCode());
                    return "redirect:/controller/purchaseList/" + id + "/list";
                });
    }

    @PostMapping("/purchaseList/{plid}/item/{id}/update")
    public Mono<String> postPurchaseListItemService(@PathVariable("plid") long plid,
                                                    @PathVariable("id") long id,
                                                    @RequestBody MultiValueMap<String, String> device,
                                                    @NotNull HttpServletRequest request) {
        LOGGER.fatal("Purchase List ITem update");
        return matchPath(request).map(path -> "http://localhost:9602/controller/purchaseList/" + plid + "/item/" + id + "/update")
                .flatMap(url -> WebClient.create().post().uri(url).body(BodyInserters.fromFormData(device)).exchange())
                .map(httpClientResponse -> {
                    LOGGER.fatal(httpClientResponse.statusCode());
                    return "redirect:/controller/purchaseList/" + plid + "/list";
                });
    }

    private static @NotNull Mono<String> matchPath(@NotNull HttpServletRequest request) {
        return Mono.just(request).map(r -> Tuples.of(r, (String) r.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE)))
                .map(tuple2 -> MATCHER.extractPathWithinPattern(tuple2.getT2(), tuple2.getT1().getRequestURI()));
    }
}
