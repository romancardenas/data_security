package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    private MessageDigest messageDigest;

    public HashGenerator(String hashFunction) throws NoSuchAlgorithmException {
        this.messageDigest = MessageDigest.getInstance(hashFunction);
    }

    public byte[] hashByteArray(byte[] text) {
        return this.messageDigest.digest(text);
    }
}
