package crypto;

import java.security.*;

public class KeyPairGen {
    private KeyPairGenerator keyGen;
    private KeyPair keyPair;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public KeyPairGen() throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(1024);
    }
    public KeyPairGen(String algorithm) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance(algorithm);
        this.keyGen.initialize(1024);
    }
    public KeyPairGen(int keyLength) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance("RSA");
        this.keyGen.initialize(keyLength);
    }
    public KeyPairGen(String algorithm, int keyLength) throws NoSuchAlgorithmException {
        this.keyGen = KeyPairGenerator.getInstance(algorithm);
        this.keyGen.initialize(keyLength);
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
    public KeyPair getKeyPair() {
        return this.keyPair;
    }

    public void createKeys() {
        this.keyPair = this.keyGen.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }
}
