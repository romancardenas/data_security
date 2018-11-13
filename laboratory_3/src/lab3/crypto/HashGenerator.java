package lab3.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashGenerator {

    public HashGenerator() {}

    /**
     * generates a random string for salting
     * @return
     */
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        return new String(bytes);
    }

    /**
     * hashes an input string with the selected technique
     * @param input
     * @param technique
     * @return
     * @throws NoSuchAlgorithmException
     */
    public byte[] getHash(String input, String technique) throws  NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(technique);
        return digest.digest(input.getBytes());
    }
}
