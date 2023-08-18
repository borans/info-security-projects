import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class FileOP {

    public final String MESSAGE_PATH = "message.txt";
    public final String USER_PATH = "Users.txt";
    private final String key = "5YBuFATucUweceMY";

    public String encrypt(String plaintext) throws Exception{

        DESKeySpec desKeySpec = new DESKeySpec(this.key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] bytes = cipher.doFinal(plaintext.getBytes("utf-8"));
        byte[] base64Bytes = Base64.getEncoder().encode(bytes);

        return new String(base64Bytes);

    }

    public byte[] encryptFile(String decrypted) throws Exception{

        byte[] decryptedBytes = decrypted.getBytes();

        DESKeySpec desKeySpec = new DESKeySpec(this.key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(decryptedBytes);

        return encryptedBytes;

    }

    public String decrypt(String ciphertext) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {

        byte[] data = Base64.getDecoder().decode(ciphertext);

        DESKeySpec dks = new DESKeySpec(this.key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(data);
        return new String(decryptedBytes);

    }

    public String decryptFile(Path path) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException {

        try {
            Files.readAllBytes(path);
        }catch (NoSuchFileException e){
            (new File(path.getFileName().toString())).createNewFile();
        }
        byte[] fileByte = Files.readAllBytes(path);

        DESKeySpec dks = new DESKeySpec(this.key.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(dks);

        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decryptedBytes = cipher.doFinal(fileByte);
        return new String(decryptedBytes);

    }
}
