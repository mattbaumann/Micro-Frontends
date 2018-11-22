package ch.hsr.apparch.recipe.model;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id = 0;

    @NonNull
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @ManyToOne
    @NotNull
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Recipe recipe;
}
