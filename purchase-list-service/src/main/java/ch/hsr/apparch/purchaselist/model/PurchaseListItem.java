package ch.hsr.apparch.purchaselist.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class PurchaseListItem {

    @Id
    @Setter(value = AccessLevel.NONE)
    private long id;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private PurchaseList list;

}
