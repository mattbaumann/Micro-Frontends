package ch.hsr.apparch.recipe.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.Validate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class Ingredient {

    @Id
    @Setter(value = AccessLevel.NONE)
    private long id = 0;

    @NonNull
    @Setter
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @Setter
    @ManyToOne
    @NotNull
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Recipe recipe;
}
