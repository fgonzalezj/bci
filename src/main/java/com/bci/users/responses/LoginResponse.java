package com.bci.users.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
  @JsonProperty("id")
  private UUID id;

  @JsonProperty("created")
  private ZonedDateTime created;

  @JsonProperty("last_login")
  private ZonedDateTime lastLogin;

  @JsonProperty("is_active")
  private boolean isActive;

  @JsonProperty("token")
  private String token;

  @JsonProperty("name")
  private String name;

  @JsonProperty("email")
  private String email;

  @JsonProperty("password")
  private String password;

  @JsonProperty("phones")
  private List<Phone> phones;
}
