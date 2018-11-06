package ch.hsr.apparch.recipe.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KitchenDeviceList {

    @JsonProperty("kitchenDevices")
    List<KitchenDevice> kitchenDevices;
}
