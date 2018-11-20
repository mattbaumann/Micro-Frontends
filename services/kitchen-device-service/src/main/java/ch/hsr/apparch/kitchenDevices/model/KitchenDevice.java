package ch.hsr.apparch.kitchenDevices.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
@SuppressWarnings("PMD.ImmutableField")
public class KitchenDevice implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String function;

    private boolean available;

    public KitchenDevice(@NotBlank @NotNull String name, @NotBlank @NotNull String function, boolean available) {
        this.name = name;
        this.function = function;
        this.available = available;
    }
}
