package com.exchange.redis;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private int id;

    private String fullName;
}
