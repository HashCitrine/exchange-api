package com.exchange.utils;

public interface JwtAndPassword {
    String hashPassword(String password);
    boolean comparePassword(String password, String hashed);
    String makeJwt(Long userId) throws Exception;
    boolean checkJwt(String jwt) throws Exception;
}
