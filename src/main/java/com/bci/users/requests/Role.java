package com.bci.users.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
}
