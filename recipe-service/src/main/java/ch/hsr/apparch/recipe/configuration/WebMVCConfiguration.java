package ch.hsr.apparch.recipe.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;


@Configuration
public class WebMVCConfiguration {

    @Value("${spring.thymeleaf.cache}")
    Boolean springThymeleafCache;

    @Bean
    public ITemplateResolver urlTemplateResolver() {
        UrlTemplateResolver resolver = new UrlTemplateResolver();
        resolver.setCacheable(springThymeleafCache);
        return resolver;
    }
}
