package com.bci.users.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Phones implements Serializable {
    private static final long serialVersionUID = -2066253186624942054L;
    @Id
    private String id;
    private String number;
    private String cityCode;
    private String countryCode;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_USER", referencedColumnName = "id", nullable = false)
    private Users users;
}
