package com.company;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class Main {

    public static void main(String[] args) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kPair = kpg.generateKeyPair();

            PrivateKey pvtKey = kPair.getPrivate();
            PublicKey pubKey = kPair.getPublic();

            System.out.println("private key: " + pvtKey.toString());
            System.out.println("public key: " + pubKey.toString());
        } catch (NoSuchAlgorithmException e) {
            System.out.println("no such algorithm found");
        }
    }
}
