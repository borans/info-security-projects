import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class TripleDES {


    public static byte[] eCBC(byte[] plainText, String key1, String key2, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {

        byte[] cipherText;

        cipherText = DES.eCBC(plainText, key1, IV);
        cipherText = DES.dCBC(cipherText, key2, IV);
        cipherText = DES.eCBC(cipherText, key1, IV);

        return cipherText;
    }


    public static byte[] dCBC(byte[] cipherText, String key1, String key2, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] plainText;

        plainText = DES.dCBC(cipherText, key1, IV);
        plainText = DES.eCBC(plainText, key2, IV);
        plainText = DES.dCBC(plainText, key1, IV);

        return plainText;

    }


    public static byte[] eCFB(byte[] plainText, String key1, String key2, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] cipherText;

        cipherText = DES.eCFB(plainText, key1, IV);
        cipherText = DES.dCFB(cipherText, key2, IV);
        cipherText = DES.eCFB(cipherText, key1, IV);

        return cipherText;

    }

    public static byte[] dCFB(byte[] cipherText, String key1, String key2, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] plainText;

        plainText = DES.dCFB(cipherText, key1, IV);
        plainText = DES.eCFB(plainText, key2, IV);
        plainText = DES.dCFB(plainText, key1, IV);

        return plainText;

    }


    public static byte[] eOFB(byte[] plainText, String key1, String key2, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] cipherText;

        cipherText = DES.eOFB(plainText, key1, IV);
        cipherText = DES.dOFB(cipherText, key2, IV);
        cipherText = DES.eOFB(cipherText, key1, IV);

        return cipherText;

    }

    public static byte[] dOFB(byte[] cipherText, String key1, String key2, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] plainText;

        plainText = DES.dOFB(cipherText, key1, IV);
        plainText = DES.eOFB(plainText, key2, IV);
        plainText = DES.dOFB(plainText, key1, IV);

        return plainText;

    }



    public static byte[] eCTR(byte[] plainText, String key1, String key2, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] cipherText;

        cipherText = DES.eCTR(plainText, key1, IV);
        cipherText = DES.dCTR(cipherText, key2, IV);
        cipherText = DES.eCTR(cipherText, key1, IV);

        return cipherText;

    }

    public static byte[] dCTR(byte[] cipherText, String key1, String key2, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] plainText;

        plainText = DES.dCTR(cipherText, key1, IV);
        plainText = DES.eCTR(plainText, key2, IV);
        plainText = DES.dCTR(plainText, key1, IV);

        return plainText;

    }
}