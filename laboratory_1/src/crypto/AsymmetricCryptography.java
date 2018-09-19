package crypto;

import java.security.*;

public class AsymmetricCryptography {
    private Signature signature;

    public AsymmetricCryptography() throws NoSuchAlgorithmException {
        this.signature = Signature.getInstance("SHA-256withRSA");
    }
    public AsymmetricCryptography(String algorithm) throws NoSuchAlgorithmException {
        this.signature = Signature.getInstance(algorithm);
    }

    byte[] signByteArray(byte[] ciphertext, PrivateKey privateKey) throws SignatureException, InvalidKeyException {
        this.signature.initSign(privateKey);
        this.signature.update(ciphertext);
        return this.signature.sign();
    }

    boolean verifySignature(byte[] ciphertext, byte[] signature, PublicKey publicKey) throws InvalidKeyException, SignatureException {
        this.signature.initVerify(publicKey);
        this.signature.update(ciphertext);
        return this.signature.verify(signature);
    }
}
