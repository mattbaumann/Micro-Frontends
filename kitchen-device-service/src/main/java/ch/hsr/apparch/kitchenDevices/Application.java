package ch.hsr.apparch.kitchenDevices;

import ch.hsr.apparch.kitchenDevices.model.KitchenDevice;
import ch.hsr.apparch.kitchenDevices.repository.KitchenDeviceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@SpringBootApplication
public class Application {

    private static final Map<String, String> SAMPLE_MACHINE_NAMES_FUNCTIONS = new HashMap<>();

    private static final Logger LOGGER = LogManager.getLogger("Application");

    public static void main(String[] args) {
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("mixer", "Mixes liquids");
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("oven", "Bakes bread");
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("steamer", "Steams vegetables");
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("refrigerator", "Stores food");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner insertSampleData(KitchenDeviceRepository lists) {
        return args -> {
            Random random = new Random();
            Map.Entry[] sample_data = SAMPLE_MACHINE_NAMES_FUNCTIONS.entrySet().toArray(new Map.Entry[]{});

            for (int i = 0; i < 5; i++) {
                Map.Entry current_entry = sample_data[random.nextInt(sample_data.length)];
                lists.save(new KitchenDevice(current_entry.getKey().toString(), current_entry.getValue().toString(), random.nextBoolean()));
            }
        };
    }

    @Bean
    public CommandLineRunner logPortAtStartup(@Value("${server.port}") int port) {
        return args -> LOGGER.info("Spring Server 'Recipe' is running under port {}", port);
    }
}
