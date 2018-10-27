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
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Setter(AccessLevel.NONE)
    private long id;

    @NonNull
    @NotBlank
    @NotNull
    @Column(nullable = false)
    private String description;

    @NonNull
    @NotNull
    @ManyToOne
    private Recipe recipe;

    @Setter
    private int device = 0;
}
