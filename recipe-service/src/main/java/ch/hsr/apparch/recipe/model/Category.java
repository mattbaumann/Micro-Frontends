package ch.hsr.apparch.recipe.model;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Entity
@Accessors(chain = true)
public class Category {

    @Id
    private long id;

    @NonNull
    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NonNull
    @OneToMany(cascade = CascadeType.DETACH)
    private Collection<Recipe> recipes;
}
