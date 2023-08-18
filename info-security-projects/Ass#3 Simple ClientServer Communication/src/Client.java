import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.stream.Collectors;

public class Client {

    private final static String username = "abt";
    private final static String serialNumber = "1234-5678-9012";

    private static String macAddress;
    private static String diskSerialNumber ;
    private static String motherboardSerialNumber;
    private static String plainText ;
    private static PublicKey publicKey;

    public static void setMacAddress(String macAddress) {
        Client.macAddress = macAddress;
    }

    public static void setDiskSerialNumber(String diskSerialNumber) {
        Client.diskSerialNumber = diskSerialNumber;
    }

    public static void setMotherboardSerialNumber(String motherboardSerialNumber) {
        Client.motherboardSerialNumber = motherboardSerialNumber;
    }

    public static void setPlainText() {

        Client.plainText = username + "$" + serialNumber + "$" + macAddress + "$" + diskSerialNumber
                + "$" + motherboardSerialNumber;
    }

    public static void setPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
        byte[] pKey = Files.readAllBytes(Paths.get("public.key"));
        X509EncodedKeySpec ks = new X509EncodedKeySpec(pKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubKey = kf.generatePublic(ks);
        Client.publicKey = pubKey;
    }

    public static void getMacAddress() throws UnknownHostException, SocketException {

        InetAddress localHost = InetAddress.getLocalHost();
        NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
        byte[] hardwareAddress = ni.getHardwareAddress();

        String[] hexadecimal = new String[hardwareAddress.length];
        for (int i = 0; i < hardwareAddress.length; i++) {
            hexadecimal[i] = String.format("%02X", hardwareAddress[i]);
        }
        String macAddress = String.join(":", hexadecimal);
        setMacAddress(macAddress);

    }

    public static void getDiskSerialNumber() throws IOException {
        setDiskSerialNumber(FileSystems.getDefault().getFileStores().iterator().next().getAttribute("volume:vsn").toString());
    }

    public static void getMotherboardSerialNumber() {
        String command = "C:\\Windows\\System32\\wbem\\WMIC.exe baseboard get serialnumber";

        String serialNumber = null;

        try {

            Process SerialNumberProcess
                    = Runtime.getRuntime().exec(command);
            InputStreamReader ISR = new InputStreamReader(
                    SerialNumberProcess.getInputStream());
            BufferedReader br = new BufferedReader(ISR);

            serialNumber = br.lines().collect(Collectors.joining()).trim().replaceAll("\\s", "").split("SerialNumber")[1];

            SerialNumberProcess.waitFor() ;
            br.close();

        }
        catch (Exception e) {
            e.printStackTrace();
            serialNumber = null;
        }
        setMotherboardSerialNumber(serialNumber);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {

        System.out.println("Client Started...");
        getMacAddress();
        System.out.println("My MAC: " + macAddress);
        getDiskSerialNumber();
        System.out.println("My Disk ID: " + diskSerialNumber);
        getMotherboardSerialNumber();
        System.out.println("My Motherboard ID: " + motherboardSerialNumber);
        setPlainText();
        setPublicKey();
        System.out.println("LicenseManager service started...");

        int returnValue = checkExist();
        if(returnValue == 0) {
            System.out.println("Client -- The license file has been broken!!");
            createLicense();
            return;
        } else if(returnValue == 1) {
            System.out.println("Client -- Succeed. The license is correct.");
        } else {
            System.out.println("Client -- License file is not found");
            createLicense();
        }

    }


    public static int checkExist() throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException {

        byte[] license;

        try {
            license = Files.readAllBytes(Paths.get("license.txt"));
        } catch (IOException e) {
            return -1;
        }

        String hashedString = hashed(plainText);
        if(verifySign(license,hashedString)) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void createLicense() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, SignatureException {

        System.out.println("Client -- Raw License Text: " + plainText);



        byte[] encryptedLicense = asymmetricEncryption(plainText);
        System.out.println("Client -- Encrypted License Text: ");
        System.out.println(new String(encryptedLicense));

        String hashedString = hashed(plainText);
        byte[] signature = LicenseManager.createSign(encryptedLicense);

        System.out.println("Client -- License is not found");


        if(verifySign(signature,hashedString)) {

            System.out.println("Client -- Succeed. The license file content is secured and signed by the server.");
            FileOutputStream f = new FileOutputStream("license.txt");
            f.write(signature);
        } else {
            System.out.println("Client -- Failed. The license file content is not secured or not signed by the server.");
        }

    }



    public static String hashed(String plainText) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] messageDigest = md.digest(plainText.getBytes());
        //convert bytearray to signum
        BigInteger  sgn = new BigInteger(1, messageDigest);
        // convert to hex value
        String hash = sgn.toString(16);
        while (hash.length() < 32) {
            hash = "0" + hash;
        }
        System.out.println("Client -- MD5 License Text: " + hash);
        return hash;

    }

    public static boolean verifySign(byte[] signature,String hashedString) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {


        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(hashedString.getBytes(StandardCharsets.UTF_8));

        return publicSignature.verify(signature);

    }


    public static byte[] asymmetricEncryption(String plainText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] license = plainText.getBytes(StandardCharsets.UTF_8);
        final byte[] encryptedLicense = encryptCipher.doFinal(license);

        return encryptedLicense;
    }
}
