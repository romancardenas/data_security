package crypto;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.*;

public class SecretKeyGenerator {

    private KeyGenerator keyGenerator;
    private SecretKey secretKey;

    public SecretKeyGenerator() throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(128);
    }
    public SecretKeyGenerator(String algorithm) throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance(algorithm);
        this.keyGenerator.init(128);
    }
    public SecretKeyGenerator(int keysize) throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance("AES");
        this.keyGenerator.init(keysize);
    }
    public SecretKeyGenerator(String algorithm, int keysize) throws NoSuchAlgorithmException {
        this.keyGenerator = KeyGenerator.getInstance(algorithm);
        this.keyGenerator.init(keysize);
    }

    public void createKey() {
        this.secretKey = this.keyGenerator.generateKey();
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    public SecretKey createSymmetricKey(String algorithm, int keyBitSize) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecureRandom secureRandom = new SecureRandom();
        keyGenerator.init(keyBitSize, secureRandom);
        return keyGenerator.generateKey();
    }
}
