package com.bci.users.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Phone {
    @JsonProperty("number")
    private String number;
    @JsonProperty("city_code")
    private String cityCode;
    @JsonProperty("country_code")
    private String countryCode;
}
