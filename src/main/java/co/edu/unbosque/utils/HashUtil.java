package co.edu.unbosque.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HashUtil {

    private HashUtil() {}

    public static String hashSHA1(String input) {
        log.debug("Generando hash SHA-1");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(input.getBytes());
            String hash = new BigInteger(1, digest).toString(16);
            return String.format("%40s", hash).replace(' ', '0');
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm not available", e);
        }
    }
}
