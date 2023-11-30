package com.bci.users.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Role {
    private String id;
    private String name;
}
