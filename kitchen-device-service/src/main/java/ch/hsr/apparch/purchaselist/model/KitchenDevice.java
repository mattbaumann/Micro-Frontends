package ch.hsr.apparch.purchaselist.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(of = {"name", "function", "available"})
@ToString(exclude = "id")
public class KitchenDevice implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Setter
    @NotBlank
    @Column(nullable = false)
    private String name;

	@Setter
    @NotBlank
    @Column
    private String function;

	@Setter
    @NotBlank
    @Column
    private boolean available;
}
