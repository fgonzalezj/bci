package com.bci.users.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@Builder
public class UserRequest {
    @Nullable
    @JsonProperty("username")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]+$", message = "Not valid email format.")
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @Nullable
    @JsonProperty("phones")
    private List<Phone> phones;
    @JsonProperty("roles")
    private List<Role> roles;
}
