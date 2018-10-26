package ch.hsr.apparch.purchaselist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString()
public class PurchaseList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id = 0;

    @NonNull
    @Setter
    @Column(nullable = false)
    @NotBlank
    private String name;

    @NonNull
    @Setter
    @Column(nullable = false)
    @FutureOrPresent
    private LocalDate date;

    @NonNull
    @Setter
    @Column()
    @OneToMany(mappedBy = "list")
    private Collection<PurchaseListItem> ingredients;

    public PurchaseList update(String name, LocalDate date) {
        this.name = name;
        this.date = date;
        return this;
    }
}
