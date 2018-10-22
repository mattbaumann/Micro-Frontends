package ch.hsr.apparch.purchaselist;

import ch.hsr.apparch.purchaselist.model.PurchaseList;
import ch.hsr.apparch.purchaselist.repository.PurchaseListItemRepository;
import ch.hsr.apparch.purchaselist.repository.PurchaseListRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Collections;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner insertSampleData(PurchaseListRepository lists, PurchaseListItemRepository listItems) {
        return args -> {
            PurchaseList list = lists.save(new PurchaseList("Sample List", LocalDate.of(2100, 10, 18), Collections.emptyList()));
        };
    }
}
