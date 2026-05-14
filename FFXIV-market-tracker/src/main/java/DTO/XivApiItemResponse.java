package DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class XivApiItemResponse {

    @JsonProperty("ID")
    private Long itemId;

    @JsonProperty("Name")
    private String itemName;

    @JsonProperty("IconHD")
    private String iconUrl;

    @JsonProperty("CanBeHq")
    private Boolean canBeHQ;

    @JsonProperty("stackSize")
    private Integer stackSize;

    @JsonProperty("PriceLow")
    private Integer vendor;
}
