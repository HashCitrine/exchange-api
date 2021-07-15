package com.exchange.utils;

import io.jsonwebtoken.*;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtAndPasswordImpl implements JwtAndPassword{

    private final String secretKey = "thisIsSecretToken";
    private final Logger logger = LoggerFactory.getLogger(JwtAndPasswordImpl.class);


    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean comparePassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

    @Override
    public String makeJwt(String userId) throws Exception {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 2);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("type", "JWT");
        headerMap.put("algo", "HS256");

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("id", userId);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expireTime)
                .signWith(signatureAlgorithm, signingKey);

        return builder.compact();
    }

    @Override
    public boolean checkJwt(String jwt) throws Exception {
        try{
            // 정상 수행되면 해당 토큰은 정상이다.
            Claims claims = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .build().parseClaimsJws(jwt).getBody();

            logger.info("expire time: " + claims.getExpiration());
            logger.info("name: " + claims.get("name"));
            logger.info("Email: " + claims.get("email"));

            return true;
        }catch(ExpiredJwtException exception){
            logger.info("토큰 만료");
            return false;
        }catch(JwtException exception){
            logger.info("토큰 변조");
            return false;
        }
    }
}
