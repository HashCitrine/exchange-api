package com.exchange.utils;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAndPassword {

    private final String secretKey = "this-is-long-long-exchange-api-secretKey-over-two-five-six-bits";

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean comparePassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    public String makeJwt(String memberId) throws Exception {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Date expireTime = new Date();
        // 20분
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 20);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("memberId", memberId);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    public boolean checkJwt(String jwt) throws Exception {
        try{
            // 정상 수행되면 해당 토큰은 정상이다.
            Claims claims = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .build().parseClaimsJws(jwt).getBody();

            log.info("expire time: " + claims.getExpiration());
            log.info("name: " + claims.get("memberId"));

            return true;
        }catch(ExpiredJwtException exception){
            log.info("토큰 만료");
            return false;
        }catch(JwtException exception){
            log.info("토큰 변조");
            return false;
        }
    }
}
