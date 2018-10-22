package ch.hsr.apparch.purchaselist;

import ch.hsr.apparch.purchaselist.model.KitchenDevice;
import ch.hsr.apparch.purchaselist.repository.KitchenDeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    public CommandLineRunner insertSampleData(KitchenDeviceRepository lists) {
        return args -> {
            KitchenDevice list = lists.save(new KitchenDevice("Sample Device", "Sample Function", true));
        };
    }
}
