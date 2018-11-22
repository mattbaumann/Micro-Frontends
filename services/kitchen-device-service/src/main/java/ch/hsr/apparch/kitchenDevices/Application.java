package ch.hsr.apparch.kitchenDevices;

import ch.hsr.apparch.kitchenDevices.model.KitchenDevice;
import ch.hsr.apparch.kitchenDevices.repository.KitchenDeviceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Log4j2
@SpringBootApplication
public class Application {

    @SuppressWarnings("PMD.UseConcurrentHashMap")
    private static final Map<String, String> SAMPLE_MACHINE_NAMES_FUNCTIONS = new HashMap<>();

    public static void main(String[] args) {
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("mixer", "Mixes liquids");
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("oven", "Bakes bread");
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("steamer", "Steams vegetables");
        SAMPLE_MACHINE_NAMES_FUNCTIONS.put("refrigerator", "Stores food");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner insertSampleData(KitchenDeviceRepository lists) {
        return args -> {
            final Random random = new Random();
            final Map.Entry[] sampleData = SAMPLE_MACHINE_NAMES_FUNCTIONS.entrySet().toArray(new Map.Entry[]{});

            for (int i = 0; i < 5; i++) {
                final Map.Entry current_entry = sampleData[random.nextInt(sampleData.length)];
                lists.save(new KitchenDevice(current_entry.getKey().toString(), current_entry.getValue().toString(), random.nextBoolean()));
            }
        };
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner logPortAtStartup(@Value("${server.port}") int port) {
        return args -> LOGGER.info("Spring Server 'Kitchen Device' is running under port {}", port);
    }
}
