package com.se2023.backend.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.se2023.backend.config.EncryptionWithKeyConfig;

public class EncryptionWithKey {
    public static String encrypt(String str, String key) {
        char[] chars = str.toCharArray();
        char[] keys = key.toCharArray();
        int len = chars.length;
        int keyLen = keys.length;
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (chars[i] ^ keys[i % keyLen]);
        }
        return new String(chars);
    }

    public static DecodedJWT decodeToken(String token) {
        //使用SHA256解密
        Algorithm algorithm = Algorithm.HMAC256(EncryptionWithKeyConfig.KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt;
    }

    public static String decrypt(String str, String key) {
        return encrypt(str, key);
    }
}
