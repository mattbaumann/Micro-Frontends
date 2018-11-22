package ch.hsr.apparch.purchaselist;

import ch.hsr.apparch.purchaselist.model.PurchaseList;
import ch.hsr.apparch.purchaselist.model.PurchaseListItem;
import ch.hsr.apparch.purchaselist.repository.PurchaseListItemRepository;
import ch.hsr.apparch.purchaselist.repository.PurchaseListRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

@Log4j2
@SpringBootApplication
public class Application {

    private static String[] SAMPLE_ITEMS = new String[]{
            "apples",
            "carrots",
            "bread",
            "milk",
            "sandwich"
    };

    private static LocalDate[] SAMPLE_DATES = new LocalDate[]{
            LocalDate.of(2100, 10, 18),
            LocalDate.of(2090, 4, 30),
            LocalDate.of(2110, 8, 14)
    };


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner insertSampleData(PurchaseListRepository lists, PurchaseListItemRepository listItems) {
        return args -> {
            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                LocalDate chosen_date = SAMPLE_DATES[random.nextInt(SAMPLE_DATES.length)];
                PurchaseList list = lists.save(new PurchaseList(chosen_date.format(DateTimeFormatter.ISO_DATE), chosen_date, new ArrayList<>(3)));

                for (int j = 0; j < 3; j++) {
                    String chosen_item_description = SAMPLE_ITEMS[random.nextInt(SAMPLE_ITEMS.length)];
                    list.getItems().add(listItems.save(new PurchaseListItem(chosen_item_description, list)));
                }
            }
        };
    }

    @Bean
    @Profile("dev")
    public CommandLineRunner logPortAtStartup(@Value("${server.port}") int port) {
        return args -> LOGGER.info("Spring Server 'Purchase List' is running under port {}", port);
    }
}
