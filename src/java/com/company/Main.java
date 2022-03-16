package com.company;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import sharding.cli.viewcontroller.CLIViewControllerClientImpl;
import sharding.cli.viewcontroller.ICLIViewControllerClient;

public class Main {

    public static void main(String[] args) {
        ICLIViewControllerClient client = new CLIViewControllerClientImpl();
        client.run();
    }
}
