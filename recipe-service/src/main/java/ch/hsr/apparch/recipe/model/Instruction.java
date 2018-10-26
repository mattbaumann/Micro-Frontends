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
public class Instruction {

    @Id
    @Setter(value = AccessLevel.NONE)
    private long id;

    @Setter
    @Column(nullable = false)
    @NotBlank
    @NotNull
    private String description;

    @Setter
    @NonNull
    @NotNull
    @ManyToOne
    private Recipe recipe;

    @Setter
    private int device = 0;
}
