package crypto;
import javax.crypto.*;
import java.io.*;
import java.security.*;


public class Main {

    private static byte[] readText(String textPath)
            throws IOException {
        File file = new File(textPath);
        FileInputStream fis = new FileInputStream(file);
        System.out.println("Total file size to read (in bytes) : " + fis.available());
        byte[] text = fis.readAllBytes();
        fis.close();
        return text;
    }

    private static String byteArrayToHex(byte[] bytes) {
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }

    private static SecretKey createSymmetricKey(String algorithm, int keyBitSize)
            throws NoSuchAlgorithmException {
        SecretKeyGenerator secretKeyGenerator = new SecretKeyGenerator(algorithm, keyBitSize);
        secretKeyGenerator.createKey();
        return secretKeyGenerator.getSecretKey();
    }

    /*
    private static KeyPair createAsymmetricKeys(String algorithm, int keyBitSize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        SecureRandom secureRandom = new SecureRandom();
        keyPairGenerator.initialize(keyBitSize, secureRandom);
        return keyPairGenerator.generateKeyPair();
    }
    */

    public static void main(String[] args) {

        String textPath = "./data/AandB.txt";
        byte[] text = null;
        try {
            text = readText(textPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String text_s = new String(text);
        System.out.println("ORIGINAL TEXT:\n" + text_s + "\n");

        // PART 1: HASH FUNCTIONS
        System.out.println("\nPART I: HASH FUNCTIONS\n");
        HashGenerator hash_md5;
        HashGenerator hash_sha1;
        HashGenerator hash_sha256;
        try {
            hash_md5  = new HashGenerator("MD5");
            hash_sha1  = new HashGenerator("SHA-1");
            hash_sha256  = new HashGenerator("SHA-256");

            byte[] digest_md5 = hash_md5.hashByteArray(text);
            byte[] digest_sha1 = hash_sha1.hashByteArray(text);
            byte[] digest_sha256 = hash_sha256.hashByteArray(text);

            System.out.println("MD5 hash value: " + byteArrayToHex(digest_md5));
            System.out.println("SHA1 hash value: " + byteArrayToHex(digest_sha1));
            System.out.println("SHA256 hash value: " + byteArrayToHex(digest_sha256));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // PART 2: SYMMETRIC ENCRYPTION
        System.out.println("\nPART II: SYMMETRIC ENCRYPTION\n");
        try {
            SecretKeyGenerator secretKeyGenerator = new SecretKeyGenerator("AES", 128);
            secretKeyGenerator.createKey();
            SecretKey secretKey = secretKeyGenerator.getSecretKey();

            SymmetricCryptography symmetricCryptography = new SymmetricCryptography("AES");
            byte[] ciphertext = symmetricCryptography.encriptByteArray(text, secretKey);
            String ciphertext_s = new String(ciphertext);
            System.out.println("CIPHERTEXT (SYMMETRIC ENCRYPTION):\n" + ciphertext_s + "\n");

            byte[] text_r = symmetricCryptography.decriptByteArray(ciphertext, secretKey);
            String text_r_s = new String(text_r);
            System.out.println("RECONSTRUCTED TEXT (SYMMETRIC ENCRYPTION):\n" + text_r_s + "\n");

            // PART 3: ASYMMETRIC ENCRYPTION
            System.out.println("\nPART III: ASYMMETRIC ENCRYPTION\n");
            KeyPairGen keyPairGen = new KeyPairGen("DSA");
            keyPairGen.createKeys();
            PrivateKey privateKey = keyPairGen.getPrivateKey();

            AsymmetricCryptography asymmetricCryptography = new AsymmetricCryptography("SHA1withDSA");
            byte[] signature = asymmetricCryptography.signByteArray(ciphertext, privateKey);
            String signature_s = new String(signature);
            System.out.println("SIGNATURE (ASYMMETRIC ENCRYPTION):\n" + signature_s + "\n");

            PublicKey publicKey = keyPairGen.getPublicKey();
            boolean verification = asymmetricCryptography.verifySignature(ciphertext, signature, publicKey);
            System.out.println("SIGNATURE VERIFICATION RESULT (ASYMMETRIC ENCRYPTION):\n" + String.valueOf(verification) + "\n");



        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }


    }
}
