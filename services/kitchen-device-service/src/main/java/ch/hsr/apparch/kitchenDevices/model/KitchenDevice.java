package ch.hsr.apparch.kitchenDevices.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
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

    @NonNull
    private boolean available;
}
