package com.bci.users.requests;

import lombok.Data;

@Data
public class Phone {
    private String number;
    private String cityCode;
    private String countryCode;
}
