package ch.hsr.apparch.recipe.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/SSFragmentCompositionController")
public class SSFragmentCompositionController {

    private static final Logger LOGGER = LogManager.getLogger("Application");
    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private static final String TARGET_SITE_KEY = "targetSite";
    private static final String TARGET_ELEMENT_KEY = "targetElement";

    private final int port;

    @Autowired
    public SSFragmentCompositionController(@Value("${server.port}") int port) {
        this.port = port;
    }

    private static String matchPath(HttpServletRequest request) {
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return MATCHER.extractPathWithinPattern(bestMatchPattern, request.getRequestURI());
    }
}
