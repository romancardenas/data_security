package crypto;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricCryptography {
    private Cipher cipher;

    public SymmetricCryptography()
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance("AES");
    }
    public SymmetricCryptography(String algorithm)
            throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.cipher = Cipher.getInstance(algorithm);

    }

    public byte[] encriptByteArray(byte[] plaintext, SecretKey secretKey)
            throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        this.cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return this.cipher.doFinal(plaintext);
    }

    public byte[] decriptByteArray(byte[] ciphertext, SecretKey secretKey)
            throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        this.cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return this.cipher.doFinal(ciphertext);
    }
}
