package com.bci.users.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
  @JsonProperty("id")
  private UUID id;

  @JsonProperty("created")
  private ZonedDateTime created;

  @JsonProperty("last_login")
  private ZonedDateTime lastLogin;

  @JsonProperty("is_active")
  private boolean isActive;
  //    private String name;
  //    private String email;
  //    private String password;
  //    private List<Phone> phones;
  //    private List<Role> roles;
}
