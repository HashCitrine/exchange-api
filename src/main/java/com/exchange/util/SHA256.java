package com.exchange.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static String encryptSHA256(String... values) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        StringBuffer result = new StringBuffer();

        for(String value : values) {
            result.append(value);
        }

        sha256.update(result.toString().getBytes());
        result = new StringBuffer();

        byte[] digests = sha256.digest();

        for(byte digest : digests) {
            result.append(Integer.toString((digest[i]&0xff) + 0x100, 16).substring(1));
        }

        return result.toString();
    }
}
