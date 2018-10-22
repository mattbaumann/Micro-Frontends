package ch.hsr.apparch.purchaselist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString(exclude = "id")
public class KitchenDevice implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Setter
    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String name;

	@Setter
    @NonNull
    @NotBlank
    @Column
    private String function;

	@Setter
    @Column
    private boolean available;

    public KitchenDevice(final String name, final String function, final boolean available) {
        this.name = name;
        this.function = function;
        this.available = available;
    }

    public KitchenDevice update(final String name, final String function, final boolean available) {
        this.name = name;
        this.function = function;
        this.available = available;
        return this;
    }
}
