package com.bci.users.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Roles implements Serializable {
    private static final long serialVersionUID = -1959439742615098581L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String name;
}
