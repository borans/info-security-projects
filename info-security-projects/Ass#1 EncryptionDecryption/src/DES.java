import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class DES {

    //xor operation for the DES modes
    private static byte[] xor(final byte[] first, final byte[] second) {

        final byte[] result = new byte[first.length];

        int s = 0;
        for (int f = 0; f < first.length; f++) {

            result[f] = (byte) (first[f] ^ second[s]);
            s++;

            if (s >= second.length) {
                s = 0;
            }
        }

        return result;
    }

    // CBC
    public static byte[] eCBC(byte[] plaintext, String key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {

        byte[] cipherText = new byte[plaintext.length];
        byte[] currIV = new byte[8];

        // current IV that will be xored with the current block of the plaintext
        System.arraycopy(IV, 0, currIV, 0, 8);

        // key initialization
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);


        for(int i=0; i < plaintext.length/8; i++){
            // first block of the plain text
            byte[] currPlain = new byte[8];
            System.arraycopy(plaintext, i*8, currPlain, 0, 8);

            currIV = cipher.doFinal(xor(currPlain, currIV));
            System.arraycopy(currIV, 0, cipherText, i*8, 8);
        }

        return cipherText;
    }


    public static byte[] dCBC(byte[] cipherText, String key, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] plainText = new byte[cipherText.length];
        byte[] currIV = new byte[8];

        // current IV that will be xored with the current block
        System.arraycopy(IV, 0, currIV, 0, 8);

        // key initialization
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);


        for(int i=0; i<cipherText.length/8; i++){
            byte[] result;
            // first block of the cipher text
            byte[] currCipher = new byte[8];
            System.arraycopy(cipherText, i*8, currCipher, 0, 8);

            result = xor(cipher.doFinal(currCipher), currIV);
            System.arraycopy(result, 0, plainText, i*8, 8);

            currIV = currCipher;
        }

        return plainText;
    }


    // CFB

    public static byte[] eCFB(byte[] plainText, String key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] cipherText = new byte[plainText.length];
        byte[] currIV = new byte[8];

        // current IV that will be xored with the current block
        System.arraycopy(IV, 0, currIV, 0, 8);

        // key initialization
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);


        for(int i=0; i<plainText.length/8; i++){
            byte[] result;
            // first block of the plain text
            byte[] currPlain = new byte[8];
            System.arraycopy(plainText, i*8, currPlain, 0, 8);

            result = xor(currPlain, cipher.doFinal(currIV));
            System.arraycopy(result, 0, cipherText, i*8, 8);

            currIV = result;

        }

        return cipherText;
    }

    public static byte[] dCFB(byte[] cipherText, String key, byte[] IV) throws UnsupportedEncodingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {

        byte[] plainText = new byte[cipherText.length];
        byte[] currIV = new byte[8];

        // current IV that will be xored with the current block
        System.arraycopy(IV, 0, currIV, 0, 8);


        // key initialization
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        for(int i=0; i<cipherText.length/8; i++){
            byte[] result;
            // first block of the cipher text
            byte[] currCipher = new byte[8];
            System.arraycopy(cipherText, i*8, currCipher, 0, 8);

            result = xor(currCipher, cipher.doFinal(currIV));
            System.arraycopy(result, 0, plainText, i*8, 8);

            currIV = currCipher;

        }

        return plainText;
    }


    // OFB

    public static byte[] eOFB(byte[] plainText, String key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {

        byte[] cipherText = new byte[plainText.length];
        byte[] currIV = new byte[8];

        // current IV that will be xored with the current block
        System.arraycopy(IV, 0, currIV, 0, 8);


        // key initialization
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);



        for(int i = 0 ; i < plainText.length / 8 ; i++){
            byte[] result;
            // first block of the plain text
            byte[] currPlain = new byte[8];
            System.arraycopy(plainText, i*8, currPlain, 0, 8);

            result = xor(currPlain, cipher.doFinal(currIV));
            System.arraycopy(result, 0, cipherText, i*8, 8);

            currIV = cipher.doFinal(currIV);
        }

        return cipherText;
    }

    public static byte[] dOFB(byte[] cipherText, String key, byte[] IV) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {

        byte[] plainText = new byte[cipherText.length];
        byte[] currIV = new byte[8];
        System.arraycopy(IV, 0, currIV, 0, 8);

        //SecretKeySpec new_key = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        for(int i = 0 ; i < cipherText.length / 8 ; i++){
            byte[] result;
            // first block of the cipher text
            byte[] currCipher = new byte[8];
            System.arraycopy(cipherText, i*8, currCipher, 0, 8);

            result = xor(currCipher, cipher.doFinal(currIV));
            System.arraycopy(result, 0, plainText, i*8, 8);

            currIV = cipher.doFinal(currIV);
        }

        return plainText;
    }

    public static byte[] eCTR(byte[] plainText, String key, byte[] nonce) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
        byte[] counterNonce = new byte[8];
        byte[] cipherText = new byte[plainText.length];
        byte[] currInput = new byte[8];
        byte[] counter = new byte[8];
        System.arraycopy(nonce, 0, counterNonce, 0, 4);


        //SecretKeySpec new_key = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        for(int i = 0 ; i < plainText.length / 8 ; i++){

            System.arraycopy(counter, 0, counterNonce, 4, 4);
            currInput = cipher.doFinal(counterNonce);
            byte[] currPlain = new byte[8];
            System.arraycopy(plainText, i*8, currPlain, 0, 8);
            System.arraycopy(xor(currInput,currPlain), 0, cipherText, i*8, 8);
            ByteBuffer wrapped = ByteBuffer.wrap(counter); // big-endian by default
            short num = wrapped.getShort(); // 1

            ByteBuffer dbuf = ByteBuffer.allocate(4);
            dbuf.putShort((short) i);
            counter = dbuf.array();
        }


        return cipherText;
    }

    public static byte[] dCTR(byte[] cipherText, String key, byte[] nonce) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeySpecException {
        byte[] counter_nonce = new byte[8];
        byte[] plainText = new byte[cipherText.length];
        byte[] currInput = new byte[8];
        byte[] counter = new byte[8];
        System.arraycopy(nonce, 0, counter_nonce, 0, 4);


        //SecretKeySpec new_key = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
        DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key secretKey = keyFactory.generateSecret(desKeySpec);
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        for(int i = 0 ; i < cipherText.length / 8 ; i++){

            System.arraycopy(counter, 0, counter_nonce, 4, 4);
            currInput = cipher.doFinal(counter_nonce);
            byte[] currCipher = new byte[8];
            System.arraycopy(cipherText, i*8, currCipher, 0, 8);
            System.arraycopy(xor(currInput,currCipher), 0, plainText, i*8, 8);
            ByteBuffer wrapped = ByteBuffer.wrap(counter); // big-endian by default
            short num = wrapped.getShort(); // 1

            ByteBuffer dbuf = ByteBuffer.allocate(4);
            dbuf.putShort((short) i);
            counter = dbuf.array();
        }
        return plainText;
    }

}
