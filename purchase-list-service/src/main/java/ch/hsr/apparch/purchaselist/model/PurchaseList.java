package ch.hsr.apparch.purchaselist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class PurchaseList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = 0;

    @Setter
    @Column(nullable = false)
    @NotBlank
    private String name;

    @Setter
    @Column(nullable = false)
    @FutureOrPresent
    private LocalDate date;
}
