package ch.hsr.apparch.purchaselist.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    @Setter(AccessLevel.NONE)
    private long id = 0;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @FutureOrPresent
    @Column(nullable = false)
    private LocalDate date;

    @NonNull
    @Column()
    @OneToMany(mappedBy = "list")
    private Collection<PurchaseListItem> ingredients;
}
