import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class LicenseManager {
    private static PrivateKey privateKey;

    public static void setPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        byte[] key = Files.readAllBytes(Paths.get("private.key"));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
        PrivateKey privKey =  keyFactory.generatePrivate(keySpec);
        LicenseManager.privateKey = privKey;
    }

    public static byte[] createSign(byte[] encryptedText) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, InvalidKeyException, SignatureException {

        System.out.println("Server -- Server is being requested...");
        System.out.println("Server -- Incoming Encrypted Text: ");
        System.out.println(new String(encryptedText));

        setPrivateKey();

        byte[] decryptedText = asymmetricDecryption(encryptedText);
        System.out.println("Server -- Decrypted Text: " + new String(decryptedText));

        String hashedString = hashed(decryptedText);
        byte[] signature;

        Signature s = Signature.getInstance("SHA256WithRSA");
        s.initSign(privateKey);
        s.update(hashedString.getBytes());
        signature = s.sign();

        System.out.println("Server -- Digital Signature: " + new String(signature));
        return signature;
    }



    public static byte[] asymmetricDecryption(byte[] encryptedText) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        return encryptCipher.doFinal(encryptedText);

    }
    public static String hashed(byte[] decryptedText) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(decryptedText);
        //convert bytearray to signum
        BigInteger  sgn = new BigInteger(1, messageDigest);
        // convert to hex value
        String hash = sgn.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        System.out.println("Server -- MD5 Plain License Text: " + hash);
        return hash;

    }

}
