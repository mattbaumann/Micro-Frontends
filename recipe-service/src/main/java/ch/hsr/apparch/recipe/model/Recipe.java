package ch.hsr.apparch.recipe.model;


import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Accessors(chain = true)
public class Recipe {


    @Id
    @Setter(value = AccessLevel.NONE)
    private long id;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @OneToMany(cascade = CascadeType.REMOVE)
    private Collection<Ingredient> ingredients;

    @NonNull
    @OneToMany(cascade = CascadeType.REMOVE)
    private Collection<Instruction> instructions;

    @NonNull
    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Category category;
}
