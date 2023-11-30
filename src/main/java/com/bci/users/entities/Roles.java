package com.bci.users.entities;

import java.io.Serializable;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Roles implements Serializable {
  private static final long serialVersionUID = -1959439742615098581L;

  @Id private String id;
  private String name;
}
