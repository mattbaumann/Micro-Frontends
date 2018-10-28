package ch.hsr.apparch.purchaselist.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class PurchaseList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDate date;

    @NonNull
    @OneToMany(mappedBy = "list", cascade = CascadeType.REMOVE)
    private Collection<PurchaseListItem> ingredients;
}
