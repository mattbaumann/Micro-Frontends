package ch.hsr.apparch.kitchenDevices.configuration;

import ch.hsr.apparch.kitchenDevices.model.KitchenDevice;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RepositoryConfiguration extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(KitchenDevice.class);
    }
}
