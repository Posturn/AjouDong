package com.example.ajoudongfe;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import java.util.Base64;
import java.util.Base64.Encoder;



public class Hmac {
    // hash 알고리즘 선택
    private static final String ALGOLISM = "HmacSHA256";
    // hash 암호화 key
    private static final String key = "nsHc6458";


    public static String hget(String message) {
        try {
            Mac hasher = Mac.getInstance(ALGOLISM);
            hasher.init(new SecretKeySpec(key.getBytes(), ALGOLISM));

            byte[] hash = hasher.doFinal(message.getBytes());
            return byteToString(hash);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        catch (InvalidKeyException e){
            e.printStackTrace();
        }
        return "";
    }

    // byte[]의 값을 16진수 형태의 문자로 변환하는 함수
    private static String byteToString(byte[] hash) {
        Encoder encoder = Base64.getEncoder();
        String encodedKey = encoder.encodeToString(hash);

        return encodedKey;

    }
}
