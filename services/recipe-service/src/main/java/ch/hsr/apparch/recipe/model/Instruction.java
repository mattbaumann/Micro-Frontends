package ch.hsr.apparch.recipe.model;


import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Entity
@NoArgsConstructor
@Accessors(chain = true)
public class Instruction implements Comparable<Instruction> {

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

    @Positive
    @Column(nullable = false)
    private long position;

    @Setter
    private int device = 0;

    public Instruction(String description, Recipe recipe, long position) {
        this.description = description;
        this.recipe = recipe;
        this.position = position;
    }

    @Override
    public int compareTo(Instruction o) {
        return (int) (this.position - o.position);
    }
}
