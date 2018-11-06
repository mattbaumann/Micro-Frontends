package ch.hsr.apparch.recipe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KitchenDevice implements Serializable {

    long id;

    @NonNull
    @NotNull
    @NotBlank
    String name;

    @NonNull
    @NotNull
    @NotBlank
    String function;

    boolean available;
}
