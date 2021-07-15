
package com.exchange.utils;

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
            result.append(Integer.toHexString(digest & 0xFF).toUpperCase());
        }

        return result.toString();
    }
}